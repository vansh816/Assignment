package z_project3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import z_project3.model.Bot;
import z_project3.repository.BotRepository;

import java.util.List;

@Service
public class BotService {
    @Autowired
    private BotRepository botRepository;
    public Bot create(Bot bot){
        return botRepository.save(bot);
    }
    public List<Bot> findall(){
        return botRepository.findAll();
    }
}
