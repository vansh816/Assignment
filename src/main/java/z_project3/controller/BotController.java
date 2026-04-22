package z_project3.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import z_project3.model.Bot;
import z_project3.model.User;
import z_project3.repository.BotRepository;
import z_project3.repository.UserRepository;
import z_project3.service.BotService;

@RestController
@RequestMapping
public class BotController {

    @Autowired
    private BotService botService;
    @Operation(summary = "Create a new Bot")
    @PostMapping("/bots")
    public ResponseEntity<?> createBot(@RequestBody Bot bot) {
        Bot saved = botService.create(bot);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }
    @Operation(summary = "Get All Bots")
    @GetMapping("/bots")
    public ResponseEntity<?> getAllBots() {
        return new ResponseEntity<>(botService.findall(), HttpStatus.OK);
    }
}
