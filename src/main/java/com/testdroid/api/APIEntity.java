package com.testdroid.api;

import com.testdroid.api.model.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 *
 * @author kajdus
 */
@XmlRootElement
@XmlSeeAlso({APIUser.class,APICluster.class,APIProject.class,APITestRun.class,APITestRunConfig.class})
public abstract class APIEntity {
    protected APIClient client;
    protected String selfURI;
    protected Long id;
        
    public APIEntity() {}

    public APIEntity(Long id) {
        this.id = id;
    }
        
    /**
     * Returns ID of entity if such ID exists.
     * Usually it does not exist for lists. Please use
     * <code>hasId()</code> method to check if ID exists.
     */
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Returns <code>true</code> if ID exists for this entity.
     */
    public boolean hasId() {
        return true;
    }
    
    protected <T extends APIEntity> APIResource<T> getResource(String uri, Class<T> type) throws APIException {
        if(client == null) {
            throw new APIException("Missing API client");
        }
        return new APIResource<T>(client, uri, type);
    }
    
    protected <T extends APIList> APIListResource<T> getListResource(String uri, Class<T> type) throws APIException {
        if(client == null) {
            throw new APIException("Missing API client");
        }
        return new APIListResource<T>(client, uri, type);
    }
    
    protected <T extends APIList> APIListResource<T> getListResource(String uri, long offset, long limit, String search, APISort sort, Class<T> type) throws APIException {
        if(client == null) {
            throw new APIException("Missing API client");
        }
        return new APIListResource<T>(client, uri, offset, limit, search, sort, type);
    }
    
    protected <T extends APIEntity> T postResource(String uri, Object body, Class<T> type) throws APIException {
        if(client == null) {
            throw new APIException("Missing API client");
        }
        return client.post(uri, body, type);
    }
    
    protected void deleteResource(String uri) throws APIException {
        if(client == null) {
            throw new APIException("Missing API client");
        }
        client.delete(uri);
    }
}
