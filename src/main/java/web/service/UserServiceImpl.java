package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDao;
import web.models.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public List<User> index() {
        List<User> list = new ArrayList<>();
        userDao.findAll().forEach(list::add);
        return list;
    }

    @Override
    @Transactional
    public User show(Long id) {
        return userDao.findById(id).get();
    }

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    @Transactional
    public void update(Long id, User user) {
        User updateUser = userDao.findById(id).get();
        if (!(user.getPass().equals(""))) {
            updateUser.setPassword(passwordEncoder.encode(user.getPass()));
        }
        updateUser.setAge(user.getAge());
        updateUser.setName(user.getName());
        updateUser.setRoles(user.getRoles());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userDao.deleteById(id);
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
       return userDao.findByName(username).get();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return user;
    }
}
