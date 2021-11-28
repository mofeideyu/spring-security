package com.security.controller;

import com.security.entity.Users;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("hello")
    public String hello() {
        return "security hello";
    }

    @GetMapping("index")
    public String index() {
        return "hello index";
    }

    @GetMapping("update")
    @Secured({"ROLE_sale","ROLE_manager"})
    public String update() {
        return "hello update";
    }

    @GetMapping("preAuthorize")
//    @PreAuthorize("hasRole('ROLE_sale')")
    @PreAuthorize("hasAnyAuthority('admin')")
    public String preAuth() {
        return "PreAuthorize";
    }

    @GetMapping("PostAuthorize")
//    @PreAuthorize("hasRole('ROLE_sale')")
    @PostAuthorize("hasAnyAuthority('admin1')")
    public String prePostAuth() {
        System.out.println("PostAuthorize");
        return "PostAuthorize";
    }

    @GetMapping("PostFilter")
//    @PreAuthorize("hasRole('ROLE_sale')")
    @PostAuthorize("hasAnyAuthority('admin')")
    @PostFilter("filterObject.userName=='a'")
    public List<Users> PostFilter() {
        List<Users> list = new ArrayList<>();
        list.add(new Users("1","a","123"));
        list.add(new Users("2","b","1234"));
        System.out.println(list);
        return list;
    }

    @GetMapping("PreFilter")
//    @PreAuthorize("hasRole('ROLE_sale')")
    @PostAuthorize("hasAnyAuthority('admin')")
    @PostFilter(value = "filterObject.ID % 2 == 0")
    public List<Users> PreFilter(List<Users> list) {
        list.forEach(t -> {
            System.out.println(t.getID() + "\t" + t.getUserName());
        });
        return list;
    }


}
