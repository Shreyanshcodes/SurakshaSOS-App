package com.example.sostry;


public class ReportsModal {
    private String ReportID;
    private String AuthenticationOfReport;
    private String ReportDetail;

    private ReportsModal(){}

    private ReportsModal(String ReportID,String AuthenticationOfReport,String SubmittedOn){
        this.ReportID = ReportID;
        this.AuthenticationOfReport=AuthenticationOfReport;
        this.ReportDetail=SubmittedOn;
    }


    public String getReportID() {
        return ReportID;
    }

    public void setReportID(String reportID) {
        ReportID = reportID;
    }

    public String getAuthenticationOfReport() {
        return AuthenticationOfReport;
    }

    public void setAuthenticationOfReport(String authenticationOfReport) {
        AuthenticationOfReport = authenticationOfReport;
    }


    public String getReportDetail() {
        return ReportDetail;
    }

    public void setReportDetail(String reportDetail) {
        ReportDetail = reportDetail;
    }
}