package com.pms.service.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.entity.PatientInfo;
import com.pms.exception.AuthenticationServiceException;
import com.pms.repository.PatientRepo;
import com.pms.service.AuthenticationService;

import jakarta.servlet.http.HttpSession;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	Random random = new Random(100000);
	
	@Autowired
	private EmailService emailService;

	
	@Autowired
	private PatientRepo patientRepo;

	@Override
	public boolean register(PatientInfo patientInfo) throws AuthenticationServiceException {
		
	
		
		String str = patientRepo.existsByEmailId(patientInfo.getEmail());
			
		
		if (str == null) {
			System.out.println(str);
			patientRepo.save(patientInfo);
			return true;
		} else {
			throw new AuthenticationServiceException("Email Already Exist");
		}

	}

	@Override
	public PatientInfo login(PatientInfo patientInfo) throws AuthenticationServiceException {
		String username = patientRepo.existsByEmailId(patientInfo.getEmail());
		
		String password = patientRepo.existsByPassword(patientInfo.getEmail());

		if(username == null || password == null) {
			throw new AuthenticationServiceException("username or password wrong");
		}

		if (patientInfo.getEmail().equals(username) && patientInfo.getPassword().equals(password)) {
			return patientRepo.getPatientInfo(patientInfo.getEmail());
		} else {
			return null;
		}

	}
	
	
	// forgot password code here...
	
	public boolean getOTP(String email, HttpSession session) throws AuthenticationServiceException{
		
		System.out.println("Email :" + email);
		
		
		String orgEmail = patientRepo.existsByEmailId(email);
		System.out.println(orgEmail);
		
		if(!email.equalsIgnoreCase(orgEmail)) {
			throw new AuthenticationServiceException("Email is not valid ");
		}
		
		

		// generate otp here

		int otp = random.nextInt(999999);

		System.out.println("OTP : " + otp);

		// Code for send email
		String subject = "OTP from Practice Management System";
		String message = "OTP for reset password :\n" + "<div style='border:1px solid #e2e2e2; padding:20px'>" + "<h1>"
				+ "OTP is : " + "<b>" + otp + "</n>" + "</h1>" + "</div>";
		String to = email;

		boolean flag = emailService.sendEmail(subject, message, to);

		if (flag) {
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			System.out.println("verify OTP");
			return true;

		} else {
			session.setAttribute("message", "Please check your email ID !!");
			return false;

		}
		
	}
	@Override
	public boolean verifyOTP(int otp, HttpSession session)throws AuthenticationServiceException{
		System.out.println("Inside service");
		int myOtp = (Integer) session.getAttribute("myotp");
		String email = (String) session.getAttribute("email");
		System.out.println(myOtp+" "+otp);
		if (myOtp == otp) {
			// change password
			PatientInfo patientInfo = this.patientRepo.getPatientInfo(email);

			if (patientInfo == null) {
				// send the error message : patient does not exist with this email
				System.out.println("Patient doesn't exist with this email");

				session.setAttribute("message", "Patient doesn't exist");
				return false;
			} else {
				System.out.println("Enter new password : ");
				return true;
			}

		} else {

			System.out.println("OTP ie wrong");
			session.setAttribute("message", "You have entered wrong otp ");
			//throw new AuthenticationServiceException("You have entered wrong otp");
			return false;
		}
		
	}
	public boolean changePassword(String newPassword, HttpSession session) throws AuthenticationServiceException{
		
		String email = (String) session.getAttribute("email");
		PatientInfo patientInfo = this.patientRepo.getPatientInfo(email);
		patientInfo.setPassword(newPassword);
		
		if(newPassword.length() < 8 ) {
			throw new AuthenticationServiceException("Password must be greater than 8 character");
		}
		
		this.patientRepo.save(patientInfo);
		System.out.println("Password reset successfully");

		return true;

		
	}
}
