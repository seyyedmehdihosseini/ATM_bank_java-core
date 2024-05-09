package basicalClass;

import java.util.List;

public interface BaseDao<E extends BaseEntity> {

    public Boolean save(E e);
    public E getById(Long id);
    public Boolean deleteById(Long id);
    public List<E> getAll();
    public List<E> getByProperty(String propertyName , Object values);
    public Boolean update(Long id , E e) throws Exception;

}
