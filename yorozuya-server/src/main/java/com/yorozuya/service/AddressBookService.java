package com.yorozuya.service;

import com.yorozuya.entity.AddressBook;
import java.util.List;

/**
 * @author Ballauma
 */
public interface AddressBookService {

    List<AddressBook> list(AddressBook addressBook);

    void save(AddressBook addressBook);

    AddressBook getById(Long id);

    void update(AddressBook addressBook);

    void setDefault(AddressBook addressBook);

    void deleteById(Long id);

}
