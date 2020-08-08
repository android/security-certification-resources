# Scoring device preload risks

An angle of analysis for the data that is collected via Hubble is to attempt to
score security and privacy risks that users are exposed to via the type and the
manner that apps are preloaded on each device/build.

In order to do this, we have developed a risk scoring framework that allows
individual risk categories to be defined and then summed as a device risk score.

In order for users to more easily grasp and make use of the final risk score, we
have adopted a fixed score framework in this iteration where a low score would
indicate a low level of risk and a high score would indicate a
high level of risk. And the score is bounded to a maximum value, of which is
currently defined to be `10`.

## General Note
We are working in tandem with a university consortium to develop a fine-grained
formula for each risk category, in addition to the identification of new
categories as we proceed.

Note that this aspect of the work (scoring) is an active work-in-progress that
may progress/evolve very quickly. We also do appreciate any feedback that you
may have for us so that we can further improve the risk scoring modules.


## Risk Categories
As of today, we have identified 3 major risk categories based on the information
that is collected via Hubble:
- Platform Signature Risk
  - Here we try to evaluate how much risk is posed by the introduction of additional
    APKs that are signed using the platform key.
- Risky Privileged Permissions Risk
  - Here we try to evaluate how much risk is posed by the pre-granting of permissions
    that is done by the OEM.
- Cleartext Traffic Risk
  - Here we try to evaluate how much risk is the user exposed to due to the APKs
    using cleartext traffic in their network communications.


## Formula
For simplicity, we state the general formula that is used across every risk
category that we have defined as follows:

` Risk = Likelihood x Damage Potential`

### Likelihood of Risk
We currently estimate the likelihood of risk using a coarse package counting.

The likelihood value is thus calculated based on a simple formula currently and
is implemented in the `_cambridge` method in `risk_analysis.py`. Expressed, the
formula for it is as follows:

` Likelihood = |A_t \ A_b| / |A_t|`

where:

`A_t` is the set of apps on a target device fulfilling a certain criteria, defined
by the risk category.

`A_b` is the set of apps on a baseline device fulfilling the same criteria, defined
by the risk category. The corresponding baseline device/build has been determined
by our algorithm to be an AOSP or GSI build of a matching API level.

`\` is the set [complement](https://en.wikipedia.org/wiki/Complement_(set_theory)) operator.

`|A|` is the [cardinality](https://en.wikipedia.org/wiki/Cardinality) of the set `A`.


### Damage Potential
Damage potential are essentially weight values that are assigned to each category.
For instance, in this iteration, we assign the damage potential of each risk
category as follows:

| Risk Categories     | Damage Potential |
|:--------------------|:-------------:|
| Platform Signature  |      6.0      |
| Risky Permissions   |      3.0      |
| Cleartext Traffic   |      1.0      |
|    **Total**        |   **10.0**    |

## Implementation
The bulk of the analysis described above is implemented in `risk_analysis.py`.
To give an example of how to call those libraries, we provided `device_scoring.py`
that is located in the same directory, i.e. `scripts/python`. In order to make
use of it, you will first need to have Hubble results collected.

Then, issue the following command on your terminal:

`python3 device_scoring.py --dir <path_to_results_directory> --normalize`

Essentially, you can copy and paste the resulting path that is outputted in the
last line of a successful execution of `automate_observation.py` (see [docs](
automate_observation.md)) and supply it as the argument to the `--dir` option.

You should always supply the `--normalize` argument so that the computation
engine would normalize the score against the base build.

Upon successful analysis, you should see outputs like below on your terminal:
```
INFO:compute_scores(159): Platform Signature Risk: X.XXXX / 6.0000
INFO:compute_scores(179): Risky Permissions Risk Score without GMS: Y.YYYY / 3.0000
INFO:compute_scores(197): Cleartext Traffic Risk Score without GMS: Z.ZZZZ / 1.0000
WARNING:main(262): Adjusted Device Risk Score (without GMS): *.** / 10.00
```