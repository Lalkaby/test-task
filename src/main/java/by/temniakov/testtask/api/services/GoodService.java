package by.temniakov.testtask.api.services;

import by.temniakov.testtask.api.dto.InGoodDto;
import by.temniakov.testtask.api.dto.OutGoodDto;
import by.temniakov.testtask.api.exceptions.InUseException;
import by.temniakov.testtask.api.exceptions.NotFoundException;
import by.temniakov.testtask.api.mappers.GoodMapper;
import by.temniakov.testtask.api.mappers.factories.SortGoodFactory;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.repositories.GoodRepository;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class GoodService {
    private final GoodRepository goodRepository;
    private final GoodMapper goodMapper;
    private final SortGoodFactory sortGoodFactory;
    private final ExampleMatcher exampleMatcherWithIgnoreIdPath;

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

    public List<OutGoodDto> findDtoByPage(
            @Min(value = 0, message = "must be not less than 0") Integer page,
            @Min(value = 1,message = "must be not less than 1")Integer size) {
        return goodRepository
                .findAll(PageRequest.of(page,size))
                .map(goodMapper::toOutDto)
                .toList();
    }


    public List<OutGoodDto> findSortedDtoByPageable(
            @PageableDefault(page = 0, size = 50) Pageable pageable){
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

    @Validated(value = {CreationInfo.class, Default.class})
    public Good createGood(
          @Valid InGoodDto createGoodDto){
        Good good = goodMapper.fromDto(createGoodDto);
        return goodRepository
                .findOne(Example.of(good))
                .orElseGet(()->goodRepository.saveAndFlush(good));
    }

    public OutGoodDto getDtoFromAddress(Good good){
        return goodMapper.toOutDto(good);
    }

    @Validated(value = {UpdateInfo.class, IdNullInfo.class, Default.class})
    public Good getUpdatedOrExistingGood(
            Integer goodId, @Valid  InGoodDto goodDto){
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
                .findOne(Example.of(cloneGood, exampleMatcherWithIgnoreIdPath))
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
