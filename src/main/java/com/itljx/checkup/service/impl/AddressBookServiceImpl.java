package com.itljx.checkup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itljx.checkup.entity.AddressBook;
import com.itljx.checkup.mapper.AddressBookMapper;
import com.itljx.checkup.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
