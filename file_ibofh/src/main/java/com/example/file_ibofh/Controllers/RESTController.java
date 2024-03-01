package com.example.file_ibofh.Controllers;

import com.example.file_ibofh.Users;
import java.io.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class RESTController {
    @GetMapping("/getUsers")
    public ArrayList<Users> getUsers(){
        final JsonMapper map = new JsonMapper();
        try {
             final ArrayList<Users> listOfUsers = map.readValue(new File("/home/user1/Рабочий стол/userList.json"),
                    new TypeReference<ArrayList<Users>>() {});
            return listOfUsers;
        }catch (IOException e){
            System.err.println(e);
        }
        return null;
    }
    @DeleteMapping("/deleteUser")
    public void deleteUser(@RequestParam String name){
        int index = -1;
        final JsonMapper map = new JsonMapper();
        ArrayList<Users> newList = new ArrayList<>();
        try {
            newList = map.readValue(new File("/home/user1/Рабочий стол/userList.json"),
                    new TypeReference<ArrayList<Users>>() {});
        }catch (IOException e){
            System.err.println(e);
        }
        for (int i = 0; i < newList.size();i++ ){
            if (name.equals(newList.get(i).getName())){
                index = i;
            }
        }
        if(index != -1) {
            newList.remove(index);
        }
        try{
            map.writeValue(new File("/home/user1/Рабочий стол/userList.json"),
                    newList);
        }catch (IOException e){
            System.err.println(e);
        }

    }
    @PostMapping("/addUser")
    public void addUser(@RequestParam("name") String name, @RequestParam("age") int age){
        boolean flag = false;
        Users userToAdd = new Users();
        final JsonMapper map = new JsonMapper();
        ArrayList<Users> newList = new ArrayList<>();
        try {
            newList = map.readValue(new File("/home/user1/Рабочий стол/userList.json"),
                    new TypeReference<ArrayList<Users>>() {});
        }catch (IOException e){
            System.err.println(e);
        }
        for (int i = 0; i < newList.size();i++ ){
            if (name.equals(newList.get(i).getName())){
                flag = true;
            }
        }
        if(flag != true) {
            userToAdd.setAge(age);
            userToAdd.setName(name);
            newList.add(userToAdd);
        }
        try{
            map.writeValue(new File("/home/user1/Рабочий стол/userList.json"),
                    newList);
        }catch (IOException e){
            System.err.println(e);
        }

    }
    @PutMapping("/updateUser")
    public void updateUser(@RequestParam("name") String name, @RequestParam("age") int age){
        int index = -1;
        Users userToAdd = new Users();
        final JsonMapper map = new JsonMapper();
        ArrayList<Users> newList = new ArrayList<>();
        try {
            newList = map.readValue(new File("/home/user1/Рабочий стол/userList.json"),
                    new TypeReference<ArrayList<Users>>() {});
        }catch (IOException e){
            System.err.println(e);
        }
        for (int i = 0; i < newList.size();i++ ){
            if (name.equals(newList.get(i).getName())){
                index = i;
            }
        }
        if(index != -1) {
            newList.remove(index);
            userToAdd.setName(name);
            userToAdd.setAge(age);
            newList.add(userToAdd);
        }
        try{
            map.writeValue(new File("/home/user1/Рабочий стол/userList.json"),
                    newList);
        }catch (IOException e){
            System.err.println(e);
        }
    }
}
