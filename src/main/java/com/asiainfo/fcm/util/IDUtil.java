package com.asiainfo.fcm.util;

/**
 * ID生成工具类.
 * Created by RUOK on 2017/6/26.
 */
public class IDUtil {

    public static final String TASK_ID_PREFIX = "T";

    public static final String ACTIVITY_ID_PREFIX = "A";

    public static final String CUSTOMER_GROUP_ID_PREFIX = "C";

    public static final String FILE_ID_PREFIX = "F";

    public static final String SC_POLICY_ACTIVITY_ID_PREFIX = "280";

    public static final String POLICY_SGMT_ID_PREFIX = "S";

    public static final String POLICY_OFFER_CODE_PREFIX = "O";

    private static synchronized long generateId() {
        return System.currentTimeMillis();
    }

    public static String generateTaskId() {
        return TASK_ID_PREFIX + generateId();
    }

    public static String generateActivityId() {
        return ACTIVITY_ID_PREFIX + generateId();
    }

    public static String generateCustomerGroupId() {
        return CUSTOMER_GROUP_ID_PREFIX + generateId();
    }

    public static String generateFileId() {
        return FILE_ID_PREFIX + generateId();
    }

    public static String generatePolicySceneActId() {
        return SC_POLICY_ACTIVITY_ID_PREFIX + generateId();
    }

    public static String generatePolicySceneActTempId(){
        return SC_POLICY_ACTIVITY_ID_PREFIX + SC_POLICY_ACTIVITY_ID_PREFIX + generateId();
    }

    public static String generatePolicySgmtId(){
        return POLICY_SGMT_ID_PREFIX + generateId();
    }

    public static String generatePolicyOfferCode(){
        return POLICY_OFFER_CODE_PREFIX + generateId();
    }
}
