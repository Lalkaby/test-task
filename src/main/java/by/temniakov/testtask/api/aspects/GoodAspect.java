package by.temniakov.testtask.api.aspects;

import by.temniakov.testtask.api.dto.OutGoodDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Log4j2
@Component
@RequiredArgsConstructor
public class GoodAspect {
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    @Pointcut("execution(public * by.temniakov.testtask.api.services.GoodService.getDtoByIdOrThrowException(..))")
    public void callAtMyServicePublic() { }

    @Before("execution(public * by.temniakov.testtask.api.services.GoodService.getDtoByIdOrThrowException(Integer)) && args(goodId)")
    public void beforeCallAtMyServicePublic(Integer goodId) {
        log.info("Good id: " + goodId);
        log.info("Execution before and yes");
    }


    @AfterReturning(value = "callAtMyServicePublic()",returning = "outGoodDto")
    public void afterReturningCallAt(OutGoodDto outGoodDto) {
        try {
            kafkaTemplate.send("good", objectMapper.writeValueAsString(outGoodDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Around(value = "callAtMyServicePublic()")
    public OutGoodDto aroundReturningCallAt(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before execution "+ joinPoint.toString());
        OutGoodDto good = (OutGoodDto) joinPoint.proceed();
        log.info(good.getTitle());
        log.info("After execution" + joinPoint.toString());
        for (var arg:joinPoint.getArgs()) {
            log.info(arg.toString());
        }

        return good;
    }

    @After(value = "callAtMyServicePublic()")
    public void afterCallAt(JoinPoint jp){
        log.info("after " + jp.toString());
    }
}
