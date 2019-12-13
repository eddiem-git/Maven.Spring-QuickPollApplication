package io.zipcoder.tc_spring_poll_application.controller;

import dtos.OptionCount;
import dtos.VoteResult;
import io.zipcoder.tc_spring_poll_application.domain.Vote;
import io.zipcoder.tc_spring_poll_application.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.*;

@RestController
public class ComputeResultController {

    private VoteRepository voteRepository;

    @Autowired
    public ComputeResultController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @RequestMapping(value = "/computeresult", method = RequestMethod.GET)
    public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
        VoteResult voteResult = new VoteResult();
        Iterable<Vote> allVotes = voteRepository.findVotesByPoll(pollId);
        Map<Long, Integer> totalVotes = new HashMap<>();
        for (Vote v : allVotes) {
            if (!totalVotes.containsKey(v.getOption().getId())) {
                totalVotes.put(v.getOption().getId(), 1);
            } else {
                totalVotes.put(v.getOption().getId(), totalVotes.get(v.getOption().getId()) + 1);
            }
        }

        int count = 0;
        Set<Long> voteKeys = totalVotes.keySet();
        List<OptionCount> optionCounts = new ArrayList<>();
        for (Long k : voteKeys) {
            optionCounts.add(new OptionCount(k, totalVotes.get(k)));
            count++;
        }
        //TODO: Implement algorithm to count votes
        voteResult.setResults(optionCounts);
        voteResult.setTotalVotes(count);
        return new ResponseEntity<VoteResult>(voteResult, HttpStatus.OK);

    }
}