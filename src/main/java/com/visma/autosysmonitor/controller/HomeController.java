package com.visma.autosysmonitor.controller;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.visma.autosysmonitor.da.SystemInfoUpdater;
import com.visma.autosysmonitor.domain.SystemInfo;
import com.visma.autosysmonitor.domain.SystemInfoTO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private SystemInfoUpdater repo;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		logger.error("Hit root context");
		repo.clear();
		repo.readFromFile("systems.txt");
		return "home";
	}

	@RequestMapping(value = "/pingSystem", method = RequestMethod.POST)
	public @ResponseBody
	SystemInfoTO pingSystem(@RequestBody SystemInfoTO system) {
		logger.info("Getting system: " + system.getName());
		SystemInfo sys = new SystemInfo(system);
		sys.update();
		SystemInfoTO retval = new SystemInfoTO(sys);
		return retval;
	}

	@RequestMapping(value = "/allSystems", method = RequestMethod.GET)
	public @ResponseBody
	SystemInfoTO[] allSystems() {
		List<SystemInfo> ret = repo.getAll();
		SystemInfoTO[] systems = new SystemInfoTO[ret.size()];
		for (int i = 0; i < ret.size(); ++i) {
			systems[i] = new SystemInfoTO(ret.get(i));
		}

		return systems;
	}

}
