package employee;

public enum EmployeeRole {
    BOSS("رییس"),
    BANK_MANAGER ("مدیر بانک"),
    BANKER("بانکدار"),
    BANK_CLERK ("کارمند بانک"),
    FINANCIAL_MANAGER("مدیر مالی"),
    SECURITY_OFFICER("افسر امنیتی"),
    CASHEIR("صندوقدار") ;

    private String title;

    EmployeeRole(String title) {
        this.title = title;
    }

    EmployeeRole() {
    }

    public String getTitle() {
        return title;
    }

}
