package by.temniakov.testtask.api.controllers;

import by.temniakov.testtask.api.dto.InGoodDto;
import by.temniakov.testtask.api.dto.OutGoodDto;
import by.temniakov.testtask.api.services.GoodOrderService;
import by.temniakov.testtask.api.services.GoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
@Tag(name="good", description = "Good management APIs")
public class GoodController {
    private final GoodService goodService;

    public static final String GET_GOOD = "/goods/{id_good}";
    public static final String FETCH_GOODS = "/goods";
    public static final String FETCH_SORTED_GOODS = "/goods/sort";
    public static final String CREATE_GOOD  = "/goods";
    public static final String DELETE_GOOD  = "/goods/{id_good}";
    public static final String UPDATE_GOOD = "/goods/{id_good}";

    @GetMapping(GET_GOOD)
    @Operation(tags = {"get"})
    public ResponseEntity<OutGoodDto> getGood(@PathVariable(name = "id_good") Integer goodId){
        return ResponseEntity.of(Optional.of(goodService.getDtoByIdOrThrowException(goodId)));
    }

    @GetMapping(FETCH_GOODS)
    @Operation(tags = {"get"})
    public ResponseEntity<List<OutGoodDto>> fetchGoods(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "50") Integer size){
        return ResponseEntity.of(Optional.of(goodService.findDtoByPage(page,size)));
    }

    @GetMapping(FETCH_SORTED_GOODS)
    @Operation(tags = {"get"})
    public ResponseEntity<List<OutGoodDto>> fetchSortedGoods(Pageable pageable){

        return ResponseEntity.of(
                Optional.of(goodService.findSortedDtoByPageable(pageable))
        );
    }

    @PostMapping(CREATE_GOOD)
    @Operation(tags = {"post"})
    public ResponseEntity<OutGoodDto> createGood(
            @RequestBody InGoodDto createGoodDto){
        OutGoodDto createdGoodDto = goodService
                .getDtoFromAddress(goodService.createGood(createGoodDto));

        return ResponseEntity.of(Optional.of(createdGoodDto));
    }

    @PatchMapping(value = UPDATE_GOOD)
    @Operation(tags = {"patch"})
    public ResponseEntity<OutGoodDto> updateGood(
            @PathVariable(name = "id_good") Integer goodId,
            @RequestBody InGoodDto goodDto){
        OutGoodDto updatedGoodDto = goodService
                .getDtoFromAddress(goodService.getUpdatedOrExistingGood(goodId, goodDto));

        return ResponseEntity.of(
                Optional.of(updatedGoodDto));
    }

    @DeleteMapping(DELETE_GOOD)
    @Operation(tags = {"delete"})
    public ResponseEntity<OutGoodDto> deleteGood(@PathVariable(name = "id_good") Integer goodId){
        goodService.delete(goodId);

        return ResponseEntity.ok().build();
    }
}
