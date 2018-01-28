package com.fs.mongo.advice;

import com.fs.mongo.annotation.Limit;
import com.fs.mongo.annotation.PageLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cnstonefang@gmail.com
 */

@Component
@Aspect
public class PageLimitAdvice {

        @Pointcut("@annotation (com.fs.mongo.annotation.PageLimit)")
        public void pointCut() {
        }

        @Around("pointCut()")
        public Object process(final ProceedingJoinPoint proceed) throws Throwable {
            final MethodSignature methodSignature = (MethodSignature) proceed.getSignature();
            final Method method = methodSignature.getMethod();
            final Object[] args = proceed.getArgs();
            final PageLimit fetchLimit = method.getAnnotation(PageLimit.class);
            final int limitPerFetch = fetchLimit.limitPerFetch();
            final List<Integer> argsNum = getParamIndexesWithSpecifiedAnnos(method
                            .getParameterAnnotations(),
                            Limit.class);
            Integer limitIndex = null;
            int limit = 0;
            if (argsNum.size() >= 1) {
                limitIndex = argsNum.get(0);
                limit = Integer.parseInt(args[limitIndex].toString());
            }

            limit = limit > limitPerFetch ? limitPerFetch : limit;
            final Object[] formatArgs;
            if (limitIndex == null) {
                formatArgs = args;
            } else {
                formatArgs = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    if (limitIndex != null && i == limitIndex) {
                        formatArgs[i] = limit;
                    } else {
                        formatArgs[i] = args[i];
                    }
                }
            }
            return proceed.proceed(formatArgs);
        }

        private List<Integer> getParamIndexesWithSpecifiedAnnos(Annotation[][]
                parameterAnnotations,Class<?> clazz) {
            List<Integer> indexes = new ArrayList();

            for(int i = 0; i < parameterAnnotations.length; ++i) {
                Annotation[] annotationsForThisParam = parameterAnnotations[i];
                if(hasSpecifiedAnno(annotationsForThisParam, clazz)) {
                    indexes.add(Integer.valueOf(i));
                }
            }
            return indexes;
        }

        private boolean hasSpecifiedAnno(Annotation[] annotations, Class<?> clazz) {
                Annotation[] anno = annotations;
                int length = annotations.length;
                for(int i = 0; i < length; ++i) {
                    Annotation annotation = anno[i];
                    if(clazz.isInstance(annotation)) {
                        return true;
                    }
                }

                return false;
            }
}
