package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.controllers.helpers.ControllerHelper;
import by.temniakov.testtask.api.dto.GoodDto;
import by.temniakov.testtask.api.exceptions.InUseException;
import by.temniakov.testtask.api.mappers.GoodMapper;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.repositories.GoodRepository;
import by.temniakov.testtask.validation.groups.CreationInfo;
import by.temniakov.testtask.validation.groups.IdNullInfo;
import by.temniakov.testtask.validation.groups.UpdateInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@Transactional
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GoodController {
    private final GoodMapper goodMapper;
    private final GoodRepository goodRepository;
    private final ControllerHelper controllerHelper;

    public static final String GET_GOOD = "/goods/{id_good}";
    public static final String FETCH_GOODS = "/goods";
    public static final String FETCH_SORTED_GOODS = "/goods/sort";
    public static final String CREATE_GOOD  = "/goods";
    public static final String DELETE_GOOD  = "/goods/{id_good}";
    public static final String UPDATE_GOOD = "/goods/{id_good}";

    @GetMapping(GET_GOOD)
    public ResponseEntity<GoodDto> getGood(@PathVariable(name = "id_good") Integer goodId){
        Good good = controllerHelper.getGoodOrThrowException(goodId);
        return ResponseEntity.of(Optional.of(good).map(goodMapper::toDto));
    }

    @GetMapping(FETCH_GOODS)
    public ResponseEntity<List<GoodDto>> fetchGoods(
            @RequestParam(name = "page", defaultValue = "0")
            @Min(value = 0, message = "must be not less than 0") Integer page,
            @RequestParam(name = "size", defaultValue = "50")
            @Min(value = 1,message = "must be not less than 1") Integer size){
        return ResponseEntity.of(
                Optional.of(
                        goodRepository
                                .findAll(PageRequest.of(page,size))
                                .map(goodMapper::toDto)
                                .toList()
                )
        );
    }

    @GetMapping(FETCH_SORTED_GOODS)
    public ResponseEntity<List<GoodDto>> fetchSortedGoods(
            @Pattern(regexp = "price|amount", message = "must be one of {regexp}")
            @RequestParam(name = "field") String field,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @Min(value = 0, message = "must be not less than 0")
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @Min(value = 1,message = "must be not less than 1")
            @RequestParam(name = "size", defaultValue = "50")Integer size){
        Sort.Direction direction = Sort.Direction.fromOptionalString(order).orElse(Sort.Direction.ASC);
        PageRequest pageRequest = PageRequest.of(page,size, direction,field);

        return ResponseEntity.of(
                Optional.of(
                        goodRepository
                                .findAll(pageRequest)
                                .map(goodMapper::toDto)
                                .toList()
                )
        );
    }

    @PostMapping(CREATE_GOOD)
    public ResponseEntity<GoodDto> createGood(
            @RequestBody @Validated(value = {CreationInfo.class, Default.class}) GoodDto createGoodDto){
        Good good = goodMapper.fromDto(createGoodDto);
        Good savedGood = goodRepository
                .findOne(Example.of(good))
                .orElseGet(()->goodRepository.saveAndFlush(good));

        return ResponseEntity.of(Optional.of(savedGood).map(goodMapper::toDto));
    }

    @PatchMapping(value = UPDATE_GOOD)
    public ResponseEntity<GoodDto> updateGood(
            @PathVariable(name = "id_good") Integer goodId,
            @Validated(value = {UpdateInfo.class, IdNullInfo.class, Default.class}) @RequestBody GoodDto goodDTO){
        Good good = controllerHelper.getGoodOrThrowException(goodId);

        Good cloneGood = goodMapper.clone(good);
        goodMapper.updateFromDto(goodDTO,cloneGood);
        Good savedGood = cloneGood;
        if (!cloneGood.equals(good)){
           savedGood =  goodRepository
                   .findOne(Example.of(cloneGood, controllerHelper.getExampleMatcherWithIgnoreIdPath()))
                   .orElseGet(()->goodRepository.saveAndFlush(cloneGood));
        }

        return ResponseEntity.of(Optional.of(savedGood).map(goodMapper::toDto));
    }

    @DeleteMapping(DELETE_GOOD)
    public ResponseEntity<GoodDto> deleteGood(@PathVariable(name = "id_good") Integer goodId){
        Good good = controllerHelper.getGoodOrThrowException(goodId);

        if (!good.getOrderAssoc().isEmpty()){
            throw new InUseException("Good is still in use",goodId);
        }

        goodRepository.delete(good);

        return ResponseEntity.ok().build();
    }
}
