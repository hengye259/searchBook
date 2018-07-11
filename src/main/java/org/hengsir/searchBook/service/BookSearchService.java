package org.hengsir.searchBook.service;


import org.hengsir.searchBook.entity.Book;

import java.util.List;
import java.util.Map;

/**
* @author 周宇恒 
* @version v1.0
* 创建时间：2017年11月7日 上午9:41:18
* 类说明:
*/

public interface BookSearchService {

	public Map<String,List<Book>> search(String bookName);
	
}
