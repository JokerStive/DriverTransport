package com.tengbo.commonlibrary.common;

public class ComponentConfig {

    //订单组件的配置项
    public static class OrderComponentConfig {

        public static final String COMPONENT_NAME = "ComponentOrder";

        public static final String ACTION_GET_HISTORY_FRAGMENT="getHistoryFragment";

        public static final String ACTION_GET_TASK_FRAGMENT="getTaskFragment";

        public static final String ACTION_GET_PROCESSING_FRAGMENT="getProcessingFragment";

    }

    // 个人中心组件的配置项
    public static class PersonalCenterComponentConfig {

        public static final String COMPONENT_NAME = "PersonalCenterComponent";

        public static final String ACTION_GET_PERSONAL_CENTER_FRAGMENT = "getPersonalCenterFragment";
    }
}
