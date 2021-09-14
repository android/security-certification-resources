# Adding new baseline for new OS release

We anticipate that every year, a new OS release will be available. In each
release, we assume that a new reference implementation (i.e.
[GSI](https://source.android.com/setup/build/gsi)) will be available.

Here, we provide a guide and a working example of how the current risk scoring 
implementation can be updated to include new baselines in order to do a proper 
comparison of a build on a new OS release.

## Steps
### Collect new data
First, download a new prebuilt official GSI image
\[[website](https://developer.android.com/topic/generic-system-image/releases#android-gsi-12)\] of the new OS version and flash 
it onto a compatible device.

Then, make sure that the device boots into the GSI image by verifying the
build string matches that of the downloaded image during runtime by navigating
to `Settings > About phone` on the device after boot.

Next, deploy `Hubble` on the device running GSI build using
`automate_observation.py` script in this repo \[[docs](automate_observation.md)\]. You should obtain results in a `results/` directory if no additional parameters 
were given when executing `automate_observation.py`.

Now, you are ready to generate a new set of baseline data with the results obtained 
in the earlier step by making use of the `generate_baseline_packages.py` script 
that is also provided in this repo. For example:
```
$ python3 generate_baseline_packages.py results/Android/gsi_arm64\:12/SPB5.210812.003/7673742\:user/release-keys/000

```

Upon successful execution of that, you should see results printed to stdout on
your terminal. You can either copy or pipe that to a file, which can be named
with the convention `<OS_Version>-GSI.json`. Then, move the json file into the
  `data/` directory located within the `scripts/python` directory.

### Modify code to update mapping
The current risk scoring implementation makes use of the baseline readings in
the `data/` directory as baselines. Therefore, there are some code changes that
is required to make sure that the code recognizes the new baseline and have
appropriate mappings updated. Here are the necessary changes:

In `package_whitelists.py`, add a mapping for the latest API level to filename, i.e. to the json file you have just generated in the earlier section.
HINT: Look at how previous levels are mapped.

Finally, you should append relevant objects/mappings to `risk_analyzers.py` that reflects the latest change in the latest version of the OS. To do so:

For each non `AbstractBasedClass` analyzer, e.g. `PlatformSignature`, you'll need
to add a new OS Level specific class at the section labeled "NOTE TO IMPLEMENTORS"
within the code. Then, add a new `if` statement that returns the OS-version specific
object. Again, refer to the existing implementation for the design pattern.

For example, if we are introducing Android S, we would first create an 
`SPlatformSignature` class by referring to how previous release's class
(`RPlatformSignature`) was implemented, and return an instance of
`SPlatformSignature` when the API level check corresponds to `31`.
