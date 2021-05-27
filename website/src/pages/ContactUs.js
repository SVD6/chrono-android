import react, { Component } from 'react';
import discord from "../assets/discord-logo.svg"

class ContactUs extends Component{
  render () {
    const { setToggle } = this.props;

    return(
      <section id="contact" className="info-section" onClick={() => {setToggle(false)}}>
        <section className="contact-title">
          <h1>Thanks for stopping by!</h1>
        </section>
        <section className="discord-content">
          <h3>Have any suggestions? Join our Discord Community!</h3>
          <a href="https://discord.gg/UbWrRxwkgR"><img src={discord} className="discord-logo" alt="discord-logo" /></a>
        </section>
        <section className="contact-content">
          <h3> Or send us your feedback @</h3>
          <h4 tabIndex="0">support@chronofit.ca</h4>
        </section>
      </section>
    )
  }
}

export default ContactUs;