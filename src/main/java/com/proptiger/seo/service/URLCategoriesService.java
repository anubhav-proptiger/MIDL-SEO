package com.proptiger.seo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.proptiger.core.enums.DomainObject;
import com.proptiger.seo.repo.URLCategoriesDao;
import com.proptiger.seo.model.URLCategories;

@Service
public class URLCategoriesService {
    
    private URLCategoriesDao urlCategoriesDao;
    
    public List<URLCategories> findAll(){
        List<URLCategories> urlCategories = urlCategoriesDao.getAllUrlCategories();
        
        if(urlCategories == null){
            return new ArrayList<URLCategories>();
        }
        return urlCategories;
    }
    
    public Map<DomainObject, List<URLCategories>> getAllUrlCategoryByDomainObject(){
        return groupURLCategoriesByObjectType(findAll());
    }
    
    private Map<DomainObject, List<URLCategories>> groupURLCategoriesByObjectType(List<URLCategories> listUrlCategories){
        Map<DomainObject, List<URLCategories>> groupMap = new HashMap<DomainObject, List<URLCategories>>();
        
        List<URLCategories> category = null;
        DomainObject domainObject = null;
        for(URLCategories urlCategories:listUrlCategories){
            domainObject = DomainObject.getFromObjectTypeName(urlCategories.getObjectType().getType());
            
            category = groupMap.get(domainObject);
            if(category == null){
                category = new ArrayList<URLCategories>();
                groupMap.put(domainObject, category);
            }
            category.add(urlCategories);
        }
        
        return groupMap;
    }
}
