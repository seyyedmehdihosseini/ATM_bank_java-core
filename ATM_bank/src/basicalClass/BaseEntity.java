package basicalClass;

import java.util.Date;

public abstract class BaseEntity {

    private Long id;
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "id=" + id
                + ", createDate=" + createDate +", ";
    }

}
