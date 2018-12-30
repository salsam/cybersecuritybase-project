package sec.project.controller;

import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    ScriptEngineManager manager = new ScriptEngineManager();

    public class Console {

        public void log(String str) {
            System.out.println("console: " + str);
        }
    }

    ScriptEngine getEngine() {
        ScriptEngine engine = manager.getEngineByName("js");
        Console console = new Console();
        engine.put("console", console);
        return engine;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model) {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address, Model model) throws ScriptException {
        // The most EFFICIENT, EASIEST and SECURE way to log stuff right?
        //getEngine().eval("console.log(\"Attempting to signup " + name + " with address: " + address + "\");");
        
        //Should be replaced with
        System.out.println("Attempting to signup " + name + " with address: " + address);

        List<Signup> signedup = signupRepository.findByAddress(address);

        if (!signedup.isEmpty()) {
            model.addAttribute("message", signedup.get(0).getName() + " has already been registered for given address");
            return "form";
        }

        signupRepository.save(new Signup(name, address));
        return "done";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("signups", signupRepository.findAll());
        return "list";
    }

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }
}
