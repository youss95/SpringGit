package kh.spring.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kh.spring.dao.MemberDAO;
import kh.spring.dto.MemberDTO;

@Controller
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private HttpSession session;
	
	@Autowired
	private MemberDAO dao;
	
	@RequestMapping("signup")
	public String signup() {
		return "member/signup";
	}
	
	@ResponseBody 
	@RequestMapping(value="duplCheck", produces="text/html;charset=utf8")
	public String duplCheck(String id) throws Exception {
		int result = dao.idCheck(id);
		return String.valueOf(result);   
	}
	
	@RequestMapping("signupProc")
	public String signupProc(MemberDTO dto) throws Exception{
		int result = dao.join(dto);
		return "redirect:/";
	}
	
	@RequestMapping("loginProc")
	public String loginProc(String id, String pw) throws Exception{
		int result = dao.login(id,pw);
		if(result>0) {
			session.setAttribute("loginId", id);
			return "redirect:/member/main";
		}else {
			return "redirect:/";
		}
	}
	
	@RequestMapping("main")
	public String memberList(Model model) throws Exception{
		List<MemberDTO> list = dao.selectAll();
		model.addAttribute("list",list);
		return "member/main";
	}
	
	//로그아웃
	@RequestMapping("logout")
	public String logout() {
		session.invalidate();
		return "redirect:/member/logoutComplete";
	}
	
	@GetMapping("logoutComplete")
	public String logoutCom() {
		return "member/logoutComplete";
	}
	
	//회원탈퇴
	@GetMapping("/signout")
	public String signout(String id) throws Exception {
		System.out.println(id);
		int result  = dao.signout(id);
		if(result > 0) {
			return "redirect:/";
		}
		return "/";
	}
	
	@ExceptionHandler
	public String exceptionHandler(Exception e) {
		e.printStackTrace();
		return "error";
	}
}
