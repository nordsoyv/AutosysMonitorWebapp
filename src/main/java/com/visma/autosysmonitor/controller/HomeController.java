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
import com.visma.autosysmonitor.domain.HttpGetSystem;
import com.visma.autosysmonitor.domain.SystemDTO;

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private SystemInfoUpdater repo;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		repo.clear();
		repo.readFromFile();
		return "home";
	}

	@RequestMapping(value = "/pingSystem", method = RequestMethod.POST)
	public @ResponseBody
	SystemDTO pingSystem(@RequestBody SystemDTO system) {
		logger.info("Getting system: " + system.getName());
		return repo.updateSystem(system).toSystemInfoDTO();
	}

	@RequestMapping(value = "/allSystems", method = RequestMethod.GET)
	public @ResponseBody
	SystemDTO[] allSystems() {
		List<HttpGetSystem> ret = repo.getAll();
		SystemDTO[] systems = new SystemDTO[ret.size()];
		for (int i = 0; i < ret.size(); ++i) {
			systems[i] = ret.get(i).toSystemInfoDTO();
		}

		return systems;
	}

}
