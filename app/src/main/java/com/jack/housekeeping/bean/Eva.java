package com.jack.housekeeping.bean;

/**
 * Created by fuyi on 2017/12/16.
 */

public class Eva {
        /**
         * evaluation : 1
         * evaluation_time : 2017-10-21 21:58:54
         * task_id : 2
         * employee_id : 369
         * customer_id : 2
         * evaluation_info : henhao
         */

        private int evaluation_id;
        private String evaluation_time;
        private int task_id;
        private String employee_id;
        private int customer_id;
        private String evaluation_info;

        public int getEvaluation() {
            return evaluation_id;
        }

        public void setEvaluation(int evaluation) {
            this.evaluation_id = evaluation;
        }

        public String getEvaluation_time() {
            return evaluation_time;
        }

        public void setEvaluation_time(String evaluation_time) {
            this.evaluation_time = evaluation_time;
        }

        public int getTask_id() {
            return task_id;
        }

        public void setTask_id(int task_id) {
            this.task_id = task_id;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(String employee_id) {
            this.employee_id = employee_id;
        }

        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(int customer_id) {
            this.customer_id = customer_id;
        }

        public String getEvaluation_info() {
            return evaluation_info;
        }

        public void setEvaluation_info(String evaluation_info) {
            this.evaluation_info = evaluation_info;
        }
}
