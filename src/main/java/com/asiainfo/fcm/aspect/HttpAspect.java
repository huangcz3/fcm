package com.asiainfo.fcm.aspect;

import com.asiainfo.fcm.entity.OperationLog;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.service.IManageService;
import com.asiainfo.fcm.util.DateUtil;
import com.asiainfo.fcm.util.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class HttpAspect {

    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    @Autowired
    private IManageService manageService;

    @AfterReturning(returning = "object", pointcut = "@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void doAfterReturning(JoinPoint joinPoint, Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        OperationLog operationLog = null;
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            HttpSession session = request.getSession();
            User user = UserUtil.getCurrentUser(session);
            String userId = user != null ? user.getUserId() : "";

            String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();

            Object[] args = joinPoint.getArgs();

            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            String[] parameterNames = methodSignature.getParameterNames();

            List<Map<String, Object>> parameterList = new ArrayList<>();

            for (int i = 0; i < args.length; i++) {

                Object arg = args[i];

                if (arg instanceof HttpSession) {
                    parameterList.add(Collections.singletonMap(parameterNames[i], "session"));
                } else if (arg instanceof ServletRequest) {
                    parameterList.add(Collections.singletonMap(parameterNames[i], "request"));
                } else if (arg instanceof ServletResponse) {
                    parameterList.add(Collections.singletonMap(parameterNames[i], "response"));
                } else if (arg instanceof MultipartFile) {
                    MultipartFile multipartFile = (MultipartFile) arg;

                    String originalFilename = multipartFile.getOriginalFilename();

                    if (originalFilename.contains("\\")) {
                        originalFilename = originalFilename.substring(originalFilename.lastIndexOf("\\") + 1);
                    }
                    parameterList.add(Collections.singletonMap(parameterNames[i], originalFilename));
                } else {
                    parameterList.add(Collections.singletonMap(parameterNames[i], arg));
                }
            }

            operationLog = new OperationLog();
            operationLog.setUserId(userId);
            operationLog.setUrl(request.getRequestURL().toString());
            operationLog.setHttpMethod(request.getMethod());
            operationLog.setClassMethod(classMethod);
            operationLog.setParameters(objectMapper.writeValueAsString(parameterList));
            operationLog.setResponse(objectMapper.writeValueAsString(object));

            String currentMonthYYYYMM = DateUtil.getCurrentMonthYYYYMMString();

            manageService.saveOperationLog(currentMonthYYYYMM, operationLog);

        } catch (Exception e) {
            logger.error("记录操作日志异常！", e);
            logger.error("日志内容：{}", operationLog);
        }
    }

}
