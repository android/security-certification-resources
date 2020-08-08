# Uraniborg

Uraniborg is a public observatory/repository that collects and hosts information
about preinstalled apps. Users can use tools in this repository to get a
"snapshot" on the state of installed devices. When used on a new device prior to
or skipping accounts set-up, the state of preinstalled packages on the device
can be obtained.

This repository currently contains code that can be used to build an explorer
app (APK) called Hubble.

It also contains code that can be used to score device preload risks. In this
effort, we collaborated with a team of university experts from the University of
Cambridge, University of Strathclyde, and Johannes Kepler University in Linz,
who created a formula that considers the risk of platform signed apps,
pregranted permissions on preloaded apps, and apps communicating using cleartext
traffic. A whitepaper of the scoring framework is also available at:
https://www.android-device-security.org/publications/

The name of this project and its components are mainly inspired by the field of
astronomy.

- [Uraniborg](https://en.wikipedia.org/wiki/Uraniborg) is a Danish
astronomical observatory.
- [Hubble](https://en.wikipedia.org/wiki/Hubble_Space_Telescope) is a space telescope used for astronomy observations.

## Documentation

Below are links to more specific documentations.

### Data Extraction
- [How to build Hubble](docs/hubble_setup.md)
- [How to use Hubble](docs/deploying_hubble.md)
- [How to automate data extraction](docs/automate_observation.md)

### Data Interpretation
- [Interpreting Hubble results](docs/hubble_results.md)

### Risk Analysis
- [Scoring device preload risks](docs/device_scoring.md)

## Disclaimer
This is not an officially supported Google product.
