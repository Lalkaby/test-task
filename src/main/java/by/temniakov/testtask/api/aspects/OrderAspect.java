package by.temniakov.testtask.api.aspects;

import by.temniakov.testtask.api.dto.OutOrderDto;
import by.temniakov.testtask.api.services.OrderEventService;
import by.temniakov.testtask.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Log4j2
@Component
@RequiredArgsConstructor
public class OrderAspect {
    private final OrderEventService orderEventService;

    @AfterReturning(
            value = "execution(public * by.temniakov.testtask.api.controllers.OrderController.changeOrderStatus(..)) && args(orderId,newStatus)",
            returning = "entity",
            argNames = "orderId,newStatus,entity"
    )
    public void afterChangeOrderStatusAddEvent(Integer orderId, String newStatus, ResponseEntity<OutOrderDto> entity) {
        Status status = Status.valueOf(newStatus);
        switch (status){
            case CANCELLED,ACTIVE -> orderEventService.saveAndFlush(status,entity.getBody());
        }
    }

    @AfterThrowing(pointcut = "execution(public * by.temniakov.testtask.api.services.OrderService.changeOrderStatus(..))", throwing = "error")
    public void afterThrowingAdvice(Throwable error){
        log.error("Throw exc",error);
    }
}
