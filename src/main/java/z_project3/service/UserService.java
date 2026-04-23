package z_project3.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import z_project3.model.User;
import z_project3.repository.UserRepository;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User create(User user){
        return userRepository.save(user);
    }
    public List<User> findall(){
        return userRepository.findAll();
    }
}
