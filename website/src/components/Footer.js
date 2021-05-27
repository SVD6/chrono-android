import react, { Component } from 'react';
import logo from '../assets/logo.svg';

class Footer extends Component{
    render() {
        const { setToggle } = this.props;

        return(
            <>
                <div className="fixed-bottom footer-section" onClick={() => {setToggle(false)}}>
                    <section className="footer-content">
                        <img src={logo} className="footer-logo" alt="logo" />
                        <table className="center">
                            <tr>
                                <p className="center">
                                    <a className="center" href="/credits"> Credits  </a>
                                    <a className="center" href="/privacy">  Privacy Policy  </a>
                                </p>
                            </tr>
                        </table>
                    </section>
                </div>
            </>
        )
    }
}

export default Footer