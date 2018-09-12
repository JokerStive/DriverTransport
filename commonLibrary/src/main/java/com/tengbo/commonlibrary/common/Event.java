package com.tengbo.commonlibrary.common;

public class Event {
    public static class ChangeTab {
        private int position;

        public ChangeTab(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

    }
}
