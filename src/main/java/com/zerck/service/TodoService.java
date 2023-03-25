package com.zerck.service;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import com.zerck.dao.TodoDAO;
import com.zerck.domain.TodoVO;
import com.zerck.dto.TodoDTO;
import com.zerck.util.MapperUtil;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public enum TodoService {
    INSTANCE;

    private TodoDAO dao;
    private ModelMapper modelMapper;

    TodoService() {
        dao = new TodoDAO();
        modelMapper = MapperUtil.INSTANCE.get();
    }

    public void register(TodoDTO todoDTO) throws Exception {
        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);
        //System.out.println("todoVO: " + todoVO);
        log.info(todoVO);

        dao.insert(todoVO); //int 를 반환하므로 이를 이용해서 예외처리도 가능
    }

    public List<TodoDTO> listAll() throws Exception {
        List<TodoVO> voList = dao.selectAll();
        log.info("voList...............");
        log.info(voList);

        List<TodoDTO> dtoList = voList.stream()
                .map(vo -> modelMapper.map(vo, TodoDTO.class))
                .collect(Collectors.toList());

        return dtoList;
    }
    public TodoDTO get(Long tno) throws Exception {
        log.info("tno: " + tno);
        TodoVO todoVO = dao.selectOne(tno); //DAO의 selectOne()으로 todoVO 객체를 가져오고  
        TodoDTO todoDTO = modelMapper.map(todoVO, TodoDTO.class); //modelMapper로 todoDTO로 변환
        return todoDTO;
    }

    public void remove(Long tno) throws Exception {
        log.info("tno: " + tno);
        dao.deleteOne(tno);
    }
    public void modify(TodoDTO todoDTO) throws Exception {
        log.info("todoDTO: " + todoDTO);
        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);

        dao.updateOne(todoVO);
    }
}
