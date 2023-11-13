package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.controllers.helpers.ControllerHelper;
import by.temniakov.testtask.api.dto.GoodDto;
import by.temniakov.testtask.api.mappers.GoodMapper;
import by.temniakov.testtask.store.entities.Good;
import by.temniakov.testtask.store.repositories.GoodRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Log4j2
@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GoodController {
    private final GoodMapper goodMapper;
    private final GoodRepository goodRepository;
    private final ControllerHelper controllerHelper;

    public static final String GET_GOOD = "/good/{id_good}";
    public static final String GET_GOODS  = "/goods";
    public static final String CREATE_GOOD  = "/goods";
    public static final String DELETE_GOOD  = "/goods/{id_good}";
    public static final String UPDATE_GOOD = "/goods/{id_good}";

    @GetMapping(GET_GOOD)
    public ResponseEntity<GoodDto> getGood(@PathVariable(name = "id_good") Integer goodId){
        Optional<Good> optionalGood = goodRepository.findById(goodId);
        return ResponseEntity.of(optionalGood.map(goodMapper::toDto));
    }

    @GetMapping(GET_GOODS)
    public ResponseEntity<GoodDto> getGoods(){
        throw  new UnsupportedOperationException("Not implemented");
    }

    @PostMapping(CREATE_GOOD)
    public ResponseEntity<GoodDto> createGood(){
        throw  new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping(DELETE_GOOD)
    public ResponseEntity<GoodDto> deleteGood(@PathVariable(name = "id_good") String goodId){
        throw  new UnsupportedOperationException("Not implemented");
    }


    @PatchMapping(value = UPDATE_GOOD)
    public ResponseEntity<GoodDto> updateGood(
            @PathVariable(name = "id_good") Integer goodId, @Valid @RequestBody GoodDto goodDTO){
        Good good = controllerHelper.getGoodOrThrowException(goodId);

        Good cloneGood = goodMapper.clone(good);
        goodMapper.updateFromDto(goodDTO,good);
        if (!cloneGood.equals(good)){
            goodRepository.saveAndFlush(good);
        }

        return ResponseEntity.of(Optional.of(good).map(goodMapper::toDto));

    }
}
