package basicalClass;

import java.util.List;

public interface BaseDao<E extends BaseEntity> {

    public Boolean save(E e);
    public E getById(Long id);
    public Boolean deleteById(Long id);
    public List<E> getAll();

}
