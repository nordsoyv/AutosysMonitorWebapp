package com.visma.autosysmonitor.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	@Autowired
	private SystemInfoUpdater repo;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! the client locale is " + locale.toString());

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		// repo.updateAll();

		model.addAttribute("serverTime", formattedDate);
		model.addAttribute("systeminfo", repo.getAll());
		return "home";
	}

	@RequestMapping(value = "/pingSystem", method = RequestMethod.POST)
	public  @ResponseBody SystemInfoTO pingSystem(@RequestBody SystemInfoTO system) {
		SystemInfo sys = new SystemInfo(system);
		sys.update();
		SystemInfoTO retval = new SystemInfoTO(sys);
		return retval;
	}

}
