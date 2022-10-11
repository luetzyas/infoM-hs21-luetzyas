package ch.zhaw.springboot.restcontroller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.zhaw.springboot.entities.Menu;
import ch.zhaw.springboot.repositories.MenuRepository;

@RestController
@CrossOrigin
public class MenuRestController {

	@Autowired
	private MenuRepository repository;

	@RequestMapping(value = "website/menus", method = RequestMethod.GET)
	public ResponseEntity<List<Menu>> getMenus() {
		List<Menu> result = this.repository.findAll();

		if (result.isEmpty()) {
			return new ResponseEntity<List<Menu>>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Menu>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "website/menus/{id}", method = RequestMethod.GET)
	public ResponseEntity<Menu> getMenuById(@PathVariable("id") long id) {
		Optional<Menu> result = this.repository.findById(id);

		if (result.isEmpty()) {
			return new ResponseEntity<Menu>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Menu>(result.get(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "website/menus/{id}", method = RequestMethod.POST)
	public ResponseEntity<Menu> createMenu(@PathVariable("id") long id, @RequestBody Menu menuRequest) {
		
		try {
			Menu result = this.repository.save(menuRequest);
			//handle menu parent
			try {
				Menu menu = repository.getOne(id);
				menu.addChild(result);
				this.repository.save(menu);
			} catch(EntityNotFoundException e) {
				System.out.println("No parent added");
			}
			
			return new ResponseEntity<Menu>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Menu>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "website/menus/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteMenuById(@PathVariable("id") long id, RedirectAttributes redirAttrs) {
		boolean exists = repository.existsById(id);

		if (exists) {
			this.repository.deleteById(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			redirAttrs.addFlashAttribute("msginfo", "ctl-todo.delete.msginfo.id-not-exist");
			redirAttrs.addFlashAttribute("requestedId", id);

			return new ResponseEntity<String>("redirect:/todo/delete" + id, HttpStatus.CONFLICT);
		}
	}
}
