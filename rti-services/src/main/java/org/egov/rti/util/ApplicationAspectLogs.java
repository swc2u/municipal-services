package org.egov.rti.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Configuration
@Slf4j
public class ApplicationAspectLogs {

	// Controller
	@Before("execution(* org.egov.rti.web.controller.*.*(..))")
	public void beforeController(JoinPoint joinPoint) {
		long startTime = System.currentTimeMillis();
		log.info("rti-services logs :: beforeController = Method::{}, Execution Time::{}", joinPoint.getSignature(),
				startTime);
		log.debug("rti-services logs :: beforeController = Method::{}, Execution Time::{}", joinPoint.getSignature(),
				startTime);
	}

	@After("execution(* org.egov.rti.web.controller.*.*(..))")
	public void afterController(JoinPoint joinPoint) {
		long startTime = System.currentTimeMillis();
		log.info("rti-services logs :: afterController = Method::{}, Execution Time::{}", joinPoint.getSignature(),
				startTime);
		log.debug("rti-services logs :: afterController = Method::{}, Execution Time::{}", joinPoint.getSignature(),
				startTime);
	}

	@AfterReturning(value = "execution(* org.egov.rti.web.controller.*.*(..))", returning = "result")
	public void afterReturningController(JoinPoint joinPoint, Object result) {
		long startTime = System.currentTimeMillis();
		log.info("rti-services logs :: afterReturningController = Method::{}, Execution Time::{}, Results::{}",
				joinPoint.getSignature(), startTime, result);
		log.debug("rti-services logs :: afterReturningController = Method::{}, Execution Time::{}, Results::{}",
				joinPoint.getSignature(), startTime, result);
	}

	@AfterThrowing(value = "execution(* org.egov.rti.web.controller.*.*(..))", throwing = "exception")
	public void afterThrowingController(JoinPoint joinPoint, Throwable exception) {
		long startTime = System.currentTimeMillis();
		log.info("rti-services logs :: afterThrowingController = Method::{}, Execution Time::{}, Exception::{}",
				joinPoint.getSignature(), startTime, exception.getMessage());
		log.debug("rti-services logs :: afterThrowingController = Method::{}, Execution Time::{}, Exception::{}",
				joinPoint.getSignature(), startTime, exception.getMessage());
	}


	// Service
		@Before("execution(* org.egov.rti.service.*.*(..))")
		public void beforeService(JoinPoint joinPoint) {
			long startTime = System.currentTimeMillis();
			log.info("rti-services logs :: beforeService = Method::{}, Execution Time::{}", joinPoint.getSignature(),
					startTime);
			log.debug("rti-services logs :: beforeService = Method::{}, Execution Time::{}", joinPoint.getSignature(),
					startTime);
		}

		@After("execution(* org.egov.rti.service.*.*(..))")
		public void afterService(JoinPoint joinPoint) {
			long startTime = System.currentTimeMillis();
			log.info("rti-services logs :: afterService = Method::{}, Execution Time::{}", joinPoint.getSignature(),
					startTime);
			log.debug("rti-services logs :: afterService = Method::{}, Execution Time::{}", joinPoint.getSignature(),
					startTime);
		}

		@AfterReturning(value = "execution(* org.egov.rti.service.*.*(..))", returning = "result")
		public void afterReturningService(JoinPoint joinPoint, Object result) {
			long startTime = System.currentTimeMillis();
			log.info("rti-services logs :: afterReturningService = Method::{}, Execution Time::{}, Results::{}",
					joinPoint.getSignature(), startTime, result);
			log.debug("rti-services logs :: afterReturningService = Method::{}, Execution Time::{}, Results::{}",
					joinPoint.getSignature(), startTime, result);
		}

		@AfterThrowing(value = "execution(* org.egov.rti.service.*.*(..))", throwing = "exception")
		public void afterThrowingService(JoinPoint joinPoint, Throwable exception) {
			long startTime = System.currentTimeMillis();
			log.info("rti-services logs :: afterThrowingService = Method::{}, Execution Time::{}, Exception::{}",
					joinPoint.getSignature(), startTime, exception.getMessage());
			log.debug("rti-services logs :: afterThrowingService = Method::{}, Execution Time::{}, Exception::{}",
					joinPoint.getSignature(), startTime, exception.getMessage());

			log.error("rti-services logs :: afterThrowingService = Method::{}, Execution Time::{}, Exception::{}",
					joinPoint.getSignature(), startTime, exception.getMessage());
		}

	// Repository
	@Before("execution(* org.egov.rti.repository.*.*(..))")
	public void beforeRepository(JoinPoint joinPoint) {
		long startTime = System.currentTimeMillis();
		log.info("rti-services logs :: beforeRepository = Method::{}, Execution Time::{}", joinPoint.getSignature(),
				startTime);
		log.debug("rti-services logs :: beforeRepository = Method::{}, Execution Time::{}", joinPoint.getSignature(),
				startTime);
	}

	@After("execution(* org.egov.rti.repository.*.*(..))")
	public void afterRepository(JoinPoint joinPoint) {
		long startTime = System.currentTimeMillis();
		log.info("rti-services logs :: afterRepository = Method::{}, Execution Time::{}", joinPoint.getSignature(),
				startTime);
		log.debug("rti-services logs :: afterRepository = Method::{}, Execution Time::{}", joinPoint.getSignature(),
				startTime);
	}

	@AfterReturning(value = "execution(* org.egov.rti.repository.*.*(..))", returning = "result")
	public void afterReturningRepository(JoinPoint joinPoint, Object result) {
		long startTime = System.currentTimeMillis();
		log.info("rti-services logs :: afterReturningRepository = Method::{}, Execution Time::{}, Results::{}",
				joinPoint.getSignature(), startTime, result);
		log.debug("rti-services logs :: afterReturningRepository = Method::{}, Execution Time::{}, Results::{}",
				joinPoint.getSignature(), startTime, result);
	}

	@AfterThrowing(value = "execution(* org.egov.rti.repository.*.*(..))", throwing = "exception")
	public void afterThrowingRepository(JoinPoint joinPoint, Throwable exception) {
		long startTime = System.currentTimeMillis();
		log.info("rti-services logs :: afterThrowingRepository = Method::{}, Execution Time::{}, Exception::{}",
				joinPoint.getSignature(), startTime, exception.getMessage());
		log.debug("rti-services logs :: afterThrowingRepository = Method::{}, Execution Time::{}, Exception::{}",
				joinPoint.getSignature(), startTime, exception.getMessage());
	}

}
