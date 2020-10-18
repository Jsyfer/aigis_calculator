package com.jsyfer.calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jsyfer.calculator.model.ExpMsg;

@RestController
public class MainController {

	private static final int[] BLACK = { 0, 0, 48, 98, 150, 203, 258, 317, 375, 437, 500, 627, 759, 897, 1040, 1187,
			1340, 1497, 1659, 1827, 2000, 2255, 2519, 2793, 3078, 3374, 3678, 3993, 4319, 4655, 5000, 5424, 5865, 6323,
			6797, 7289, 7797, 8322, 8865, 9423, 10001, 10848, 11729, 12644, 13593, 14577, 15593, 16644, 17729, 18848,
			20000, 21272, 22593, 23966, 25391, 26865, 28391, 29966, 31593, 33272, 35000, 36695, 38457, 40289, 42186,
			44153, 46187, 48288, 50457, 52695, 55001, 57119, 59322, 61610, 63983, 66441, 68984, 71610, 74322, 77118,
			80000, 82967, 86051, 89255, 92577, 96017, 99576, 103254, 107051, 110966, 115001, 117966, 121052, 124254,
			127577, 131018, 134577, 138254, 142052, 145967 };

	private static final int[] PLATINUM = { 0, 0, 45, 91, 140, 189, 241, 295, 350, 407, 466, 585, 708, 837, 970, 1107,
			1250, 1397, 1548, 1705, 1866, 2104, 2351, 2607, 2873, 3149, 3433, 3727, 4031, 4344, 4666, 5062, 5474, 5901,
			6343, 6803, 7277, 7767, 8274, 8795, 9334, 10125, 10947, 11801, 12687, 13605, 14553, 15534, 16547, 17591,
			18666, 19853, 21087, 22368, 23698, 25074, 26498, 27968, 29487, 31053, 32666, 34248, 35893, 37603, 39374,
			41209, 43107, 45069, 47093, 49182, 51334, 53311, 55367, 57502, 59717, 62012, 64385, 66836, 69367, 71977,
			74666, 77435, 80314, 83304, 86405, 89615, 92938, 96370, 99914, 103568, 107334, 110102, 112981, 115970,
			119071, 122283, 125605, 129037, 132581, 136235 };

	private static final int[] GOLD = { 0, 0, 42, 85, 130, 176, 224, 274, 325, 378, 433, 543, 658, 777, 901, 1028, 1161,
			1297, 1438, 1583, 1733, 1954, 2183, 2421, 2668, 2924, 3188, 3461, 3743, 4034, 4333, 4701, 5083, 5480, 5890,
			6317, 6757, 7212, 7683, 8167, 8667, 9402, 10165, 10958, 11781, 12633, 13514, 14425, 15365, 16335, 17333,
			18435, 19581, 20770, 22005, 23283, 24605, 25970, 27381, 28835, 30333, 31802, 33329, 34917, 36561, 38266,
			40028, 41850, 43729, 45669, 47667, 49503, 51412, 53395, 55452, 57582, 59786, 62062, 64412, 66836, 69333,
			71904, 74577, 77354, 80233, 83214, 86299, 89487, 92777, 96170, 99667, 102237, 104911, 107687, 110566,
			113549, 116633, 119820, 123111, 126504 };

	@PostMapping("/calculator")
	public ExpMsg expCalculator(@RequestBody ExpMsg expMsg) {
		int requireExp = 0;
		int targetLv = expMsg.getTargetLv();
		int currentLv = expMsg.getCurrentLv();
		int expToNextLv = expMsg.getExpToNextLv();
		switch (expMsg.getRarity()) {
		case "gold":
			requireExp = GOLD[targetLv] - GOLD[currentLv + 1] + expToNextLv;
			expMsg = calculate(requireExp, 18000, expMsg);
			expMsg.setRequireExp(requireExp);
			break;
		case "platinum":
			requireExp = PLATINUM[targetLv] - PLATINUM[currentLv + 1] + expToNextLv;
			expMsg = calculate(requireExp, 19000, expMsg);
			expMsg.setRequireExp(requireExp);
			break;
		case "black":
			requireExp = BLACK[targetLv] - BLACK[currentLv + 1] + expToNextLv;
			expMsg = calculate(requireExp, 20000, expMsg);
			expMsg.setRequireExp(requireExp);
			break;
		}

		return expMsg;
	}

	private ExpMsg calculate(int requireExp, int syukufukuSeireiExp, ExpMsg expMsg) {
		boolean useBlackArmor = expMsg.isUseBlackArmor();
		boolean useSyufukuSeirei = expMsg.isUseSyufukuSeirei();
		boolean usePurezeSeirei = expMsg.isUsePurezeSeirei();
		boolean useCaffeSeirei = expMsg.isUseCaffeSeirei();

		int platinumArmorLimitExp = 8000 * expMsg.getPlatinumArmorLimitExpNum();
		int blackArmorNum;
		int platinumArmorNum;
		int syukufukuSeireiNum;
		int purezeSeireiNum;
		int caffeSeireiNum;
		if (useBlackArmor) {
			blackArmorNum = requireExp / 40000;
			requireExp = requireExp % 40000;
			expMsg.setBlackArmorNum(blackArmorNum);
		}
		if (8000 <= requireExp && requireExp <= platinumArmorLimitExp) {
			platinumArmorNum = requireExp / 8000;
			requireExp = requireExp % 8000;
			expMsg.setPlatinumArmorNum(platinumArmorNum);
		} else if (platinumArmorLimitExp < requireExp) {
			if (useSyufukuSeirei) {
				syukufukuSeireiNum = requireExp / syukufukuSeireiExp;
				requireExp = requireExp % syukufukuSeireiExp;
				expMsg.setSyukufukuSeireiNum(syukufukuSeireiNum);
			}
			if (requireExp < 8000) {
				return calculateRest(requireExp, expMsg);
			}
			if (usePurezeSeirei) {
				purezeSeireiNum = requireExp / 18000;
				requireExp = requireExp % 18000;
				expMsg.setPurezeSeireiNum(purezeSeireiNum);
			}
			if (requireExp < 8000) {
				return calculateRest(requireExp, expMsg);
			}
			if (useCaffeSeirei) {
				caffeSeireiNum = requireExp / 10000;
				requireExp = requireExp % 10000;
				expMsg.setCaffeSeireiNum(caffeSeireiNum);
			}
			if (requireExp < 8000) {
				return calculateRest(requireExp, expMsg);
			}
			platinumArmorNum = requireExp / 8000;
			requireExp = requireExp % 8000;
			expMsg.setPlatinumArmorNum(platinumArmorNum);
		}

		return calculateRest(requireExp, expMsg);

	}

	// 铜铁狗粮数量计算
	private ExpMsg calculateRest(int requireExp, ExpMsg expMsg) {
		int exp295Num = 0;
		int exp285Num = 0;
		int exp265Num = 0;
		int exp255Num = 0;
		int exp100Num = 0;
		int exp70Num = 0;
		int expNum;
		// 补全使能被5整除
		int tempNum = requireExp % 5;
		if (tempNum != 5) {
			requireExp = requireExp + 5 - tempNum;
		}
		expNum = requireExp / 550;
		requireExp = requireExp % 550;

		if (requireExp <= 70) {
			exp70Num = 1;
			expMsg.setExp70Num(exp70Num);
		} else if (requireExp <= 100) {
			exp100Num = 1;
			expMsg.setExp100Num(exp100Num);
		} else if (requireExp <= 140) {
			exp70Num = 2;
			expMsg.setExp70Num(exp70Num);
		} else if (requireExp <= 170) {
			exp70Num = 1;
			exp100Num = 1;
			expMsg.setExp70Num(exp70Num);
			expMsg.setExp100Num(exp100Num);
		} else if (requireExp <= 200) {
			exp100Num = 2;
			expMsg.setExp100Num(exp100Num);
		} else if (requireExp <= 210) {
			exp70Num = 3;
			expMsg.setExp70Num(exp70Num);
		} else if (requireExp <= 240) {
			exp100Num = 1;
			exp70Num = 3;
			expMsg.setExp70Num(exp70Num);
			expMsg.setExp100Num(exp100Num);
		} else if (requireExp <= 255) {
			exp255Num = 1;
			expMsg.setExp255Num(exp255Num);
		} else if (requireExp <= 265) {
			exp265Num = 1;
			expMsg.setExp265Num(exp265Num);
		} else if (requireExp <= 285) {
			exp285Num = 1;
			expMsg.setExp285Num(exp285Num);
		} else if (requireExp <= 295) {
			exp295Num = 1;
			expMsg.setExp295Num(exp295Num);
		} else if (requireExp <= 300) {
			exp100Num = 3;
			expMsg.setExp100Num(exp100Num);
		} else if (requireExp <= 400) {
			exp100Num = 4;
			expMsg.setExp100Num(exp100Num);
		} else if (requireExp < 450) {
			exp255Num = 1;
			exp100Num = 2;
			expMsg.setExp255Num(exp255Num);
			expMsg.setExp100Num(exp100Num);
		} else if (requireExp < 510) {
			exp255Num = 2;
			expMsg.setExp255Num(exp255Num);
		}
//		List<List<Integer>> resList = combinationSum(candidates, target);
//
//		System.out.println(resList);
		expMsg.setExpNum(expNum);
		return expMsg;
	}

	private List<List<Integer>> combinationSum(int[] candidates, int target) {
		Arrays.sort(candidates);
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		getResult(result, new ArrayList<Integer>(), candidates, target, 0);
		return result;
	}

	private void getResult(List<List<Integer>> result, List<Integer> cur, int candidates[], int target, int start) {
		if (target > 0) {
			for (int i = start; i < candidates.length && target >= candidates[i]; i++) {
				cur.add(candidates[i]);
				getResult(result, cur, candidates, target - candidates[i], i);
				cur.remove(cur.size() - 1);
			} // for
		} // if
		else if (target == 0) {
			result.add(new ArrayList<Integer>(cur));
		} // else if
	}

}
