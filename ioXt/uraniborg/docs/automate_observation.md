# How to automate data extraction

## Prerequisites
Please follow the same prerequisites on [deploying hubble](deploying_hubble.md).

Additionally, you need to make sure that you have `Python 3.5` or above installed
on your system.

## Using the script
You will be able to find a number of Python 3 scripts in the `scripts/python`
directory.

In order to automate the data extraction process, which is covered by
[deploying hubble](deploying_hubble.md), you will be able to make use of the
`automate_observation.py` python script. Make sure that your test/target device
is connected via ADB, and issue the following command from your terminal:

`python3 automate_observations.py`

Upon successful execution, you should see messages like:
```
INFO:automate_observation.py:main(560): SUCCESS! Hubble was successfully deployed and executed on connected device ABCDEF012345.
INFO:automate_observation.py:main(561): Hubble output files can be found at: some/valid/path
```

If you see the final SUCCESS message, feel free to ignore earlier ERROR messages.
Those resulted from some files that cannot be extracted from device, but does not
affect the core files required for further analysis.
