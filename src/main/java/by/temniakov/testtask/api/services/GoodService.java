package by.temniakov.testtask.api.services;

import by.temniakov.testtask.api.dto.InGoodDto;
import by.temniakov.testtask.api.dto.OutGoodDto;
import by.temniakov.testtask.api.exceptions.InUseException;
import by.temniakov.testtask.api.exceptions.NotFoundException;
import by.temniakov.testtask.api.mappers.GoodMapper;
import by.temniakov.testtask.api.mappers.factories.SortGoodFactory;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.repositories.GoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return goodRepository
                .findById(goodId)
                .orElseThrow(()->
                        new NotFoundException("Good doesn't exists.", goodId)
                );
    }

    public void checkExistsAndThrowException(Integer goodId) {
        if (!goodRepository.existsById(goodId)){
            throw new NotFoundException("Good doesn't exists.", goodId);
        }
    }

    public List<OutGoodDto> findDtoByPage(Integer page,Integer size) {
        return goodRepository
                .findAll(PageRequest.of(page,size))
                .map(goodMapper::toOutDto)
                .toList();
    }


    public List<OutGoodDto> findSortedDtoByPageable(Pageable pageable){
        Sort newSort = Sort.by(pageable.getSort()
                .filter(order -> sortGoodFactory.getFilterKeys().contains(order.getProperty()))
                .map(sortGoodFactory::fromJsonSortOrder)
                .toList());
        PageRequest newPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), newSort);

        return goodRepository
                .findAll(newPage)
                .map(goodMapper::toOutDto)
                .toList();
    }

    public Good createGood(InGoodDto createGoodDto){
        Good good = goodMapper.fromDto(createGoodDto);
        return goodRepository
                .findOne(Example.of(good))
                .orElseGet(()->goodRepository.saveAndFlush(good));
    }


    public OutGoodDto getDtoFromGood(Good good){
        return goodMapper.toOutDto(good);
    }

    public Good getUpdatedOrExistingGood(
            Integer goodId, InGoodDto goodDto){
        Good good = getByIdOrThrowException(goodId);

        Good cloneGood = goodMapper.clone(good);
        goodMapper.updateFromDto(goodDto,cloneGood);
        Good savedGood = cloneGood;

        if (!cloneGood.equals(good)){
            savedGood = getUpdatedOrExistingGood(cloneGood);
        }

        return savedGood;
    }

    private Good getUpdatedOrExistingGood(Good cloneGood){
        return goodRepository
                .findOne(Example.of(cloneGood, ExampleMatcher.matching().withIgnorePaths("id")))
                .orElseGet(()->goodRepository.saveAndFlush(cloneGood));
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
}
