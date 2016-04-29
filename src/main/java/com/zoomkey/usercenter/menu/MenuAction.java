
package com.zoomkey.usercenter.menu;

import com.zoomkey.core.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MenuAction extends BaseAction {

    protected MenuBo menuBo;

    protected List<Menu> menuList = new ArrayList<Menu>();

    private Long userId;


    @Autowired
    public void setMenuBo(MenuBo menuBo) {
        this.menuBo = menuBo;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<Menu> getMenuList() {
        return this.menuList;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
