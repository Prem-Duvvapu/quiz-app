package com.example.question_service.service;

import com.example.question_service.dao.QuestionDao;
import com.example.question_service.model.Question;
import com.example.question_service.model.QuestionWrapper;
import com.example.question_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();;
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public List<Question> getQuestionsByCategory(String category) {
        return questionDao.findByCategory(category);
    }

    public void addOrUpdateQuestion(Question question) {
        questionDao.save(question);
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, int numQuestions) {
        List<Integer> questionIds=questionDao.findRandomQuestionsByCategory(category,numQuestions);
        return new ResponseEntity<>(questionIds,HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionsIds) {
        List<QuestionWrapper> wrappers=new ArrayList<>();
        List<Question> questions=new ArrayList<>();

        for (int id: questionsIds) {
            Question q=questionDao.findById(id).get();
            questions.add(q);
        }

        for (Question q: questions) {
            QuestionWrapper wrapper=new QuestionWrapper();
            wrapper.setId(q.getId());
            wrapper.setQuestionTitle(q.getQuestionTitle());
            wrapper.setOption1(q.getOption1());
            wrapper.setOption2(q.getOption2());
            wrapper.setOption3(q.getOption3());
            wrapper.setOption4(q.getOption4());
            wrappers.add(wrapper);
        }

        return new ResponseEntity<>(wrappers,HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        int score=0;

        for (Response response: responses) {
            Question question=questionDao.findById(response.getId()).get();

            if (response.getResponse().equals(question.getRightAnswer()))
                score++;
        }

        return new ResponseEntity<>(score,HttpStatus.OK);
    }
}
