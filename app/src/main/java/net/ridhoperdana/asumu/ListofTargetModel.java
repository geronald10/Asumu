package net.ridhoperdana.asumu;

import android.graphics.Bitmap;

/**
 * Created by RIDHO on 9/7/2017.
 */

public class ListofTargetModel {
    String targetTitle;
    String targetImageUrl;
    String targetDescription;
    String targetAmount;
    String targetStartDate;
    String targetDueDate;
    String targetOffset;
    String targetStatus;
    String targetNormalExpense;
    String targetId;

    public ListofTargetModel(String targetTitle, String targetImageUrl, String targetDescription, String targetAmount, String targetStartDate, String targetDueDate, String targetOffset, String targetStatus, String targetId) {
        this.targetTitle = targetTitle;
        this.targetImageUrl = targetImageUrl;
        this.targetDescription = targetDescription;
        this.targetAmount = targetAmount;
        this.targetStartDate = targetStartDate;
        this.targetDueDate = targetDueDate;
        this.targetOffset = targetOffset;
        this.targetStatus = targetStatus;
        this.targetId = targetId;
    }

    public void setTargetNormalExpense(String targetNormalExpense) {
        this.targetNormalExpense = targetNormalExpense;
    }

    public String getTargetTitle() {
        return targetTitle;
    }

    public String getTargetImageUrl() {
        return targetImageUrl;
    }

    public String getTargetDescription() {
        return targetDescription;
    }

    public String getTargetAmount() {
        return targetAmount;
    }

    public String getTargetStartDate() {
        return targetStartDate;
    }

    public String getTargetDueDate() {
        return targetDueDate;
    }

    public String getTargetOffset() {
        return targetOffset;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    public String getTargetNormalExpense() {
        return targetNormalExpense;
    }

    public String getTargetId() {
        return targetId;
    }
}
