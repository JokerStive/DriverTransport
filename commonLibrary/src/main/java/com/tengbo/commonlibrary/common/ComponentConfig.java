package com.tengbo.commonlibrary.common;

public class ComponentConfig {

    //订单组件的配置项
    public static class Order {

        public static final String COMPONENT_NAME = "ComponentOrder";

        public static final String ACTION_GET_HOME_PAGE_FRAGMENTS = "getHomePageFragments";

        public static final String ACTION_GET_HISTORY_FRAGMENT = "getHistoryFragment";

        public static final String ACTION_GET_TASK_FRAGMENT = "getTaskFragment";

        public static final String ACTION_GET_DUTY_FRAGMENT = "getDutyFragment";

        public static final String ACTION_GET_PROCESSING_FRAGMENT = "getProcessingFragment";

        public static final String ACTION_GET_INFO_FRAGMENT = "getInfoFragment";

    }

    //main组件
    public static class Main {

        public static final String COMPONENT_NAME = "MainComponent";

        public static final String ACTION_OPEN_MAIN_ACTIVITY = "openMainActivity";

        public static final String ACTION_OPEN_LOGIN_ACTIVITY = "openLoginActivity";

        public static final String ACTION_CHANGE_TAB = "changeTab";

        public static final String PARAM_TAB_POSITION = "tabPosition";

        public static final String PARAM_IS_GOMain = "isGoMain";
        public static final String ACTION_SHOW_DUTY = "showDuty";
    }


    // 个人中心组件的配置项
    public static class PersonalCenter {

        public static final String COMPONENT_NAME = "PersonalCenterComponent";

        public static final String ACTION_GET_PERSONAL_CENTER_FRAGMENT = "getPersonalCenterFragment";
    }
}
