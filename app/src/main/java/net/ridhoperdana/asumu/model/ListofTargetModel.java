package net.ridhoperdana.asumu.model;

/**
 * Created by RIDHO on 9/7/2017.
 */

public class ListofTargetModel {
    String target_desc;
    String targetImageUrl;
    String target_amount;
    String target_startdate;
    String target_duedate;
    String offset;
    String status;
    String normal_expense;
    String id_target;

    public ListofTargetModel(String target_desc, String targetImageUrl, String target_amount, String target_startdate, String target_duedate, String offset, String status, String id_target) {
        this.target_desc = target_desc;
        this.targetImageUrl = targetImageUrl;
        this.target_amount = target_amount;
        this.target_startdate = target_startdate;
        this.target_duedate = target_duedate;
        this.offset = offset;
        this.status = status;
        this.id_target = id_target;
    }

    public void setNormal_expense(String normal_expense) {
        this.normal_expense = normal_expense;
    }

    public String getTarget_desc() {
        return target_desc;
    }

    public String getTargetImageUrl() {
        return targetImageUrl;
    }

    public String getTarget_amount() {
        return target_amount;
    }

    public String getTarget_startdate() {
        return target_startdate;
    }

    public String getTarget_duedate() {
        return target_duedate;
    }

    public String getOffset() {
        return offset;
    }

    public String getStatus() {
        return status;
    }

    public String getNormal_expense() {
        return normal_expense;
    }

    public String getId_target() {
        return id_target;
    }
}
