package com.mht.modules.sys.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.mht.modules.sys.utils.ContextHolderUtils;
import com.mht.modules.sys.utils.EhcacheUtil;

/***
 * 在线用户管理**
 * 
 * @ClassName: ClientManager
 * @Description:
 * @author com.mhout.wj
 * @date 2017年4月20日 下午5:10:41
 * @version 1.0.0
 */
public class ClientManager {
    private final String CACHENAME = "userCache";
    private final String OnlineClientsKey = "onLineClients";

    private static ClientManager instance = new ClientManager();

    private ClientManager() {

    }

    public static ClientManager getInstance() {
        return instance;
    }

    /**
     * 向ehcache缓存中增加Client对象
     *
     * @author xugj
     */
    @SuppressWarnings("unchecked")
    private boolean addClientToCachedMap(String sessionId, Client client) {
        HashMap<String, Client> onLineClients;
        if (EhcacheUtil.get(CACHENAME, OnlineClientsKey) == null) {
            onLineClients = new HashMap<String, Client>();
        } else {
            onLineClients = (HashMap<String, Client>) EhcacheUtil.get(CACHENAME, OnlineClientsKey);
        }
        onLineClients.put(sessionId, client);
        EhcacheUtil.put(CACHENAME, OnlineClientsKey, onLineClients);
        return true;
    }

    /**
     * 从缓存中的Client集合中删除 Client对象
     */
    @SuppressWarnings("unchecked")
    private boolean removeClientFromCachedMap(String sessionId) {
        HashMap<String, Client> onLineClients;
        if (EhcacheUtil.get(CACHENAME, OnlineClientsKey) != null) {
            onLineClients = (HashMap<String, Client>) EhcacheUtil.get(CACHENAME, OnlineClientsKey);
            onLineClients.remove(sessionId);
            EhcacheUtil.put(CACHENAME, OnlineClientsKey, onLineClients);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 用户登录，向session中增加用户信息
     *
     * @param sessionId
     * @param client
     */
    public void addClinet(String sessionId, Client client) {
        ContextHolderUtils.getSession().setAttribute(sessionId, client);
        if (client != null) {
            Client ret = new Client();
            ret.setIp(client.getIp());
            ret.setLogindatetime(client.getLogindatetime());
            ret.setUser(client.getUser());
            addClientToCachedMap(sessionId, ret);
        }
    }

    /**
     * 用户退出登录 从Session中删除用户信息 sessionId
     */
    public void removeClinet(String sessionId) {
        ContextHolderUtils.getSession().removeAttribute(sessionId);
        Enumeration<String> names = ContextHolderUtils.getSession().getAttributeNames();
        if (names.hasMoreElements())
            System.out.println(names.nextElement());
        removeClientFromCachedMap(sessionId);
    }

    /**
     * 根据sessionId 得到Client 对象
     *
     * @param sessionId
     */
    public Client getClient(String sessionId) {
        if (!StringUtils.isEmpty(sessionId) && ContextHolderUtils.getSession().getAttribute(sessionId) != null) {
            return (Client) ContextHolderUtils.getSession().getAttribute(sessionId);
        } else {
            return null;
        }
    }

    /**
     * 得到Client 对象
     */
    public Client getClient() {
        String sessionId = ContextHolderUtils.getSession().getId();
        if (!StringUtils.isEmpty(sessionId) && ContextHolderUtils.getSession().getAttribute(sessionId) != null) {
            return (Client) ContextHolderUtils.getSession().getAttribute(sessionId);
        } else {
            return null;
        }
    }

    /**
     * 得到所有在线用户
     */
    @SuppressWarnings("unchecked")
    public Collection<Client> getAllClient() {
        if (EhcacheUtil.get(CACHENAME, OnlineClientsKey) != null) {// 这里get为
                                                                   // null
            HashMap<String, Client> onLineClients = (HashMap<String, Client>) EhcacheUtil.get(CACHENAME,
                    OnlineClientsKey);
            return onLineClients.values();
        } else
            return new ArrayList<Client>();
    }
}
