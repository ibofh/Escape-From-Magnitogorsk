package com.example.file_ibofh.Controllers;

import com.example.file_ibofh.Users;
import java.io.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

@RestController
public class RESTController {
    @GetMapping("/getUsers")
    public ArrayList<Users> getUsers(@RequestParam(required = false) String searchedName, @RequestParam(required = false) Integer age,
                                     @RequestParam(required = false) String paramToSort, @RequestParam(required = false) Integer ammountToReturn,
                                     @RequestParam(required = false) Integer ammountToSkip){
        final JsonMapper map = new JsonMapper();
        try {
            int Counter = 0;
            final ArrayList<Users> listOfUsers = map.readValue(new File("src/Utils/userList.json"),
                    new TypeReference<ArrayList<Users>>() {});
            final ArrayList<Users> listToReturn = new ArrayList<>();
            if (searchedName != null){
                for (Users user : listOfUsers){
                    if (user.getName().contains(searchedName)){
                        listToReturn.add(user);
                    }
                }
                Counter++;
            }
            if(Counter == 0){
                listToReturn.addAll(listOfUsers);
            }
            if(age != null){
                ArrayList<Users> buffferList = new ArrayList<>();
                for(Users user : listToReturn){
                    if(user.getAge().equals(age)) {
                        buffferList.add(user);
                    }
                }
                listToReturn.clear();
                listToReturn.addAll(buffferList);
                Counter++;
            }
            /*if(Counter == 0){
                for (Users user : listOfUsers){
                    listToReturn.add(user);
                }
            }*/
            if(paramToSort != null){
                HashMap<String, Integer> mapForSortWithNameKey = new HashMap<>();
                HashMap<Integer, String> mapForSortWithAgeKey = new HashMap<>();
                ArrayList<String> bufferListNames = new ArrayList<>();
                ArrayList<Integer> bufferListAges = new ArrayList<>();
                ArrayList<Users> bufferListToReturn = new ArrayList<>();
                for (Users user : listToReturn){
                    mapForSortWithNameKey.put(user.getName(), user.getAge());
                    mapForSortWithAgeKey.put(user.getAge(), user.getName());
                    bufferListNames.add(user.getName());
                    bufferListAges.add(user.getAge());
                }
                switch (paramToSort){
                    case ("nameUp"):
                        Collections.sort(bufferListNames);
                        for (String Name : bufferListNames){
                            bufferListToReturn.add(new Users(Name, mapForSortWithNameKey.get(Name)));
                        }
                        listToReturn.clear();
                        listToReturn.addAll(bufferListToReturn);
                        break;
                    case ("nameDown"):
                        Collections.sort(bufferListNames);
                        Collections.reverse(bufferListNames);
                        for (String Name : bufferListNames){
                            bufferListToReturn.add(new Users(Name, mapForSortWithNameKey.get(Name)));
                        }
                        listToReturn.clear();
                        listToReturn.addAll(bufferListToReturn);
                        break;
                    case ("ageUp"):
                        Collections.sort(bufferListAges);
                        for (int ageNew : bufferListAges){
                            bufferListToReturn.add(new Users(mapForSortWithAgeKey.get(ageNew), ageNew));
                        }
                        listToReturn.clear();
                        listToReturn.addAll(bufferListToReturn);
                        break;
                    case ("ageDown"):
                        Collections.sort(bufferListAges);
                        Collections.reverse(bufferListAges);
                        for (int ageNew : bufferListAges){
                            bufferListToReturn.add(new Users(mapForSortWithAgeKey.get(ageNew), ageNew));
                        }
                        listToReturn.clear();
                        listToReturn.addAll(bufferListToReturn);
                        break;
                }
            }
            if(ammountToSkip != null){
                if(ammountToSkip > listToReturn.size()){
                    ammountToSkip = listToReturn.size();
                }
                while (ammountToSkip > 0){
                    listToReturn.remove(ammountToSkip-1);
                    ammountToSkip -= 1;
                }
            }
            if(ammountToReturn != null){
                if (ammountToReturn > listToReturn.size()){
                    ammountToReturn = listToReturn.size();
                }
                ArrayList<Users> bufferListOfUsers = new ArrayList<>();
                    while(ammountToReturn > 0){
                        bufferListOfUsers.add(listToReturn.get(ammountToReturn - 1));
                        ammountToReturn -= 1;
                    }
                    listToReturn.clear();
                    listToReturn.addAll(bufferListOfUsers);
                    Collections.reverse(listToReturn);
            }


            return listToReturn;

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
            newList = map.readValue(new File("src/Utils/userList.json"),
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
            map.writeValue(new File("src/Utils/userList.json"),
                    newList);
        }catch (IOException e){
            System.err.println(e);
        }

    }
    @PostMapping("/addUser")
    public void addUser(@RequestBody Users user){
        boolean flag = false;
        Users userToAdd = new Users();
        final JsonMapper map = new JsonMapper();
        ArrayList<Users> newList = new ArrayList<>();
        try {
            newList = map.readValue(new File("src/Utils/userList.json"),
                    new TypeReference<ArrayList<Users>>() {});
        }catch (IOException e){
            System.err.println(e);
        }
        for (int i = 0; i < newList.size();i++ ){
            if (user.getName().equals(newList.get(i).getName())){
                flag = true;
            }
        }
        if(flag != true) {
            userToAdd.setAge(user.getAge());
            userToAdd.setName(user.getName());
            newList.add(userToAdd);
        }
        try{
            map.writeValue(new File("src/Utils/userList.json"),
                    newList);
        }catch (IOException e){
            System.err.println(e);
        }

    }
    @PutMapping("/updateUser")
    public void updateUser(@RequestBody Users user){
        int index = -1;
        Users userToAdd = new Users();
        final JsonMapper map = new JsonMapper();
        ArrayList<Users> newList = new ArrayList<>();
        try {
            newList = map.readValue(new File("src/Utils/userList.json"),
                    new TypeReference<ArrayList<Users>>() {});
        }catch (IOException e){
            System.err.println(e);
        }
        for (int i = 0; i < newList.size();i++ ){
            if (user.getName().equals(newList.get(i).getName())){
                index = i;
            }
        }
        if(index != -1) {
            newList.remove(index);
            userToAdd.setName(user.getName());
            userToAdd.setAge(user.getAge());
            newList.add(userToAdd);
        }
        try{
            map.writeValue(new File("src/Utils/userList.json"),
                    newList);
        }catch (IOException e){
            System.err.println(e);
        }
    }
}
