package by.temniakov.testtask.api.services;

import by.temniakov.testtask.api.dto.InGoodDto;
import by.temniakov.testtask.api.dto.OutGoodDto;
import by.temniakov.testtask.api.exceptions.InUseException;
import by.temniakov.testtask.api.exceptions.InvalidStockLevelException;
import by.temniakov.testtask.api.exceptions.NotFoundException;
import by.temniakov.testtask.api.mappers.GoodMapper;
import by.temniakov.testtask.api.mappers.factories.SortGoodFactory;
import by.temniakov.testtask.enums.Status;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.repositories.GoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodService {
    private final GoodRepository goodRepository;
    private final GoodMapper goodMapper;
    private final SortGoodFactory sortGoodFactory;

    public OutGoodDto getDtoByIdOrThrowException(Integer goodId){
        return goodMapper
                .toOutDto(getByIdOrThrowException(goodId));
    }

    public Good getByIdOrThrowException(Integer goodId){
        Good good = goodRepository
                .findById(goodId)
                .orElseThrow(()->
                        new NotFoundException("Good doesn't exists.", goodId)
                );

        fillNumberOrders(List.of(good));

        return good;
    }

    public void checkExistsAndThrowException(Integer goodId) {
        if (!goodRepository.existsById(goodId)){
            throw new NotFoundException("Good doesn't exists.", goodId);
        }
    }

    public List<OutGoodDto> findDtoByPage(Integer page,Integer size) {
        List<Good> goods = findAllBy(PageRequest.of(page,size));
        return goods
                .stream()
                .map(goodMapper::toOutDto)
                .toList();
    }

    private List<Good> findAllBy(PageRequest page) {
        List<Good> goods = goodRepository.findAllBy(page);
        fillNumberOrders(goods);
        return goods;
    }

    private void fillNumberOrders(List<Good> goods) {
        Map<Integer, Integer> numberOrder = getNumberOrders(
                goods.stream().map(Good::getId).toList());
        goods.forEach(good -> good
                .setNumberOrders(numberOrder.get(good.getId())));
    }

    private Map<Integer, Integer> getNumberOrders(List<Integer> goodIds){
    return goodRepository
            .getCountGoodByOrderStatus(goodIds,Status.COMPLETED)
            .stream()
            .collect(Collectors.toMap(
                    x->x.get(0, Integer.class),
                    x->x.get(1, Long.class).intValue()));
    }

    public List<OutGoodDto> findSortedDtoByPageable(Pageable pageable){
        Sort newSort = Sort.by(pageable.getSort()
                .filter(order -> sortGoodFactory.getFilterKeys().contains(order.getProperty()))
                .map(sortGoodFactory::fromJsonSortOrder)
                .toList());
        PageRequest newPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), newSort);

        return findAllBy(newPage)
                .stream()
                .map(goodMapper::toOutDto)
                .toList();
    }

    public Good createGood(InGoodDto createGoodDto){
        Good good = goodMapper.fromDto(createGoodDto);
        return getExistingOrCreateNew(good);
    }

    private Good getExistingOrCreateNew(Good good) {
        Good example = Good.builder()
                .producer(good.getProducer())
                .title(good.getTitle())
                .currency(good.getCurrency())
                .build();

        Good savedGood = goodRepository
                .findOne(Example.of(example))
                .orElseGet(() -> goodRepository.saveAndFlush(good));

        fillNumberOrders(List.of(savedGood));

        return savedGood;
    }
    
    public OutGoodDto getDtoFromGood(Good good){
        return goodMapper.toOutDto(good);
    }

    public void delete(Integer goodId){
        checkExistsAndThrowException(goodId);

        checkInUseAndThrowException(goodId);

        deleteById(goodId);
    }

    private void deleteById(Integer goodId){
        goodRepository.deleteById(goodId);
    }

    private void checkInUseAndThrowException(Integer goodId){
        if (goodRepository.countOrdersWithGoodById(goodId)!=0){
            throw new InUseException("Good is still in use",goodId);
        }
    }

    public void saveAllAndFlush(Iterable<Good> entities){
        goodRepository.saveAllAndFlush(entities);
    }

    public List<Good> findAllById(Iterable<Integer> entities){
        return goodRepository.findAllById(entities);
    }

    public List<Integer> getExistingIds(List<Integer> goodIds) {
        return goodRepository.getExistingIds(goodIds);
    }

    @Transactional
    public Good changeStockGoods(Integer id, Integer diffAmount) {
        Good good = getByIdOrThrowException(id);
        Integer newAmount= good.getAmount()+diffAmount;
        if (newAmount<0){
            throw new InvalidStockLevelException(
                    "Goods amount can't be less than 0",
                    good.getId(),
                    good.getAmount());
        }

        good.setAmount(newAmount);

        return good;
    }
}
