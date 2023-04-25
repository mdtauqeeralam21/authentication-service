package com.pms.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.entity.OTPRequest;
import com.pms.entity.PatientInfo;
import com.pms.exception.AuthenticationServiceException;
import com.pms.service.AuthenticationService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:4200","https://d2sp5bzn9zomrp.cloudfront.net/"}, allowedHeaders = "*", allowCredentials = "true")
public class PatientRestController {


	@Autowired
	private AuthenticationService service;


	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody PatientInfo patientDetails) throws AuthenticationServiceException {
		boolean bool = false;

		bool = this.service.register(patientDetails);
		if (bool) {
			System.out.println(bool);
			return ResponseEntity.status(HttpStatus.CREATED).body(bool);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PostMapping("/login")
	public ResponseEntity<PatientInfo> login(@RequestBody PatientInfo patientInfo) throws AuthenticationServiceException {
		PatientInfo pInfo = null;

		pInfo = this.service.login(patientInfo);
		System.out.println(patientInfo);
		if (pInfo != null) {
			System.out.println(pInfo);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(pInfo);
		} else {
			throw new AuthenticationServiceException("username or password wrong");
		}

	}

	// Use for sending otp to patient from forgot button on UI

	@PostMapping("/send-otp")
	public ResponseEntity<?> getOTP(@RequestBody PatientInfo patientInfo , HttpSession session) throws AuthenticationServiceException {
		
		boolean result = service.getOTP(patientInfo.getEmail(), session);
		if(result) {		
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(true);
		} else {
			session.setAttribute("message", "Please check your email ID !!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);

		}
	}

	// verify otp from verify button on UI

	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOTP(@RequestBody OTPRequest otpRequest, HttpSession session) throws AuthenticationServiceException  {
		int otp = Integer.parseInt(otpRequest.getOtp());
		System.out.println("inside controller"+otp);
		boolean result = service.verifyOTP(otp, session);
		
		if(result) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(true);
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(false);
		}
	}

	// change password call by UI newPassword from
	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody String newPassword, HttpSession session) throws AuthenticationServiceException {
		
		boolean result = false;
		result = service.changePassword(newPassword, session);
		if(result) {
			return ResponseEntity.status(HttpStatus.OK).body(true);
		}
		else {
		return ResponseEntity.status(HttpStatus.OK).body(false);
		}

	}

}
