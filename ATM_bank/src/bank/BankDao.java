package bank;

import dao.BaseDaoImpl;

public class BankDao extends BaseDaoImpl<Bank> {
    public BankDao(Class<Bank> entityClass) {
        super(entityClass);
    }
    public BankDao() {
        super(Bank.class);
    }

}
