package com.samteam.teammate.global.init;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BaseInitData implements CommandLineRunner {
	private final BaseInitDataService baseInitDataService;

	@Override
	public void run(String... args) {
		baseInitDataService.initData();
	}
}
