package com.kh.memberjpa.controller;

import com.kh.memberjpa.dao.MemberDao;
import com.kh.memberjpa.entity.Member;
import com.kh.memberjpa.form.MemberForm;
import com.kh.memberjpa.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Controller
public class MemberController {

    @Autowired
    private MemberRepository repository;

    @RequestMapping("/list")
    public ModelAndView list(MemberForm memberForm, ModelAndView mav) {
        memberForm.setNewMember(true);
        Iterable<Member> memberList = repository.findAll();
        mav.addObject("memberList", memberList);
        mav.addObject("title", "회원목록");
        mav.setViewName("list");
        return mav;
    }

    @GetMapping("/insert")
    public ModelAndView insert(@ModelAttribute Member member, ModelAndView mav, MemberForm memberForm) {
        memberForm.setNewMember(true);
        mav.addObject("member", member);
        mav.addObject("title", "회원가입");
        mav.setViewName("reg");
        return mav;
    }

    @PostMapping("/insert")
    public ModelAndView insertPost(@ModelAttribute @Validated Member member, BindingResult bindingResult, ModelAndView mav) {
        if(bindingResult.hasErrors()) {
            mav.addObject("message", "입력내용을 확인하세요.");
            mav.setViewName("reg");
            return mav;
        }
        repository.saveAndFlush(member);
        mav = new ModelAndView("redirect:/list");
        return mav;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("num") Integer num, ModelAndView mav) {
        repository.deleteById(num);
        mav.addObject("message", "삭제가 완료되었습니다.");
        return "redirect:/list";
    }

    @Autowired
    private MemberDao memberDao;

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        memberDao = new MemberDao(entityManager);
    }

    @GetMapping("/search")
    public ModelAndView search(ModelAndView mav, @RequestParam("id") String id, @RequestParam("name") String name, @RequestParam("email") String email) {
        Iterable<Member> memberList = memberDao.find(id, name, email);
        mav.addObject("memberList", memberList);
        mav.addObject("title", "검색결과");
        mav.setViewName("list");
        return mav;
    }

    @GetMapping("/{num}")
    public String showEdit(MemberForm memberForm, @PathVariable("num") Integer num, Model model) {
        Optional<Member> memberOptional = repository.findByNum(num);
        Optional<MemberForm> memberFormOptional = memberOptional.map(t ->makeMemberForm(t));

        if(memberFormOptional.isPresent())
            memberForm = memberFormOptional.get();

        makeUpdateModel(memberForm, model);
        return "reg";
    }

    private MemberForm makeMemberForm(Member member) {
        MemberForm form = new MemberForm();
        form.setNum(member.getNum());
        form.setId(member.getId());
        form.setPass(member.getPass());
        form.setName(member.getName());
        form.setEmail(member.getEmail());

        return form;
    }

    private void makeUpdateModel(MemberForm memberForm, Model model) {
        model.addAttribute("num", memberForm.getNum());
        memberForm.setNewMember(false);
        model.addAttribute("memberForm", memberForm);
        model.addAttribute("title", "회원정보 수정");
    }

    @PostMapping("/update")
    public String update(@Validated MemberForm memberForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Member member = makeMember(memberForm);
        if(!bindingResult.hasErrors()) {
            repository.saveAndFlush(member);
            redirectAttributes.addFlashAttribute("complete", "변경이 완료되었습니다.");
            return "redirect:/list";
        } else {
            makeUpdateModel(memberForm, model);
            return "reg";
        }
    }

    private  Member makeMember(MemberForm memberForm) {
        Member member = new Member();
        member.setNum(memberForm.getNum());
        member.setId(memberForm.getId());
        member.setPass(memberForm.getPass());
        member.setName(memberForm.getName());
        member.setEmail(memberForm.getEmail());

        return member;
    }
}
